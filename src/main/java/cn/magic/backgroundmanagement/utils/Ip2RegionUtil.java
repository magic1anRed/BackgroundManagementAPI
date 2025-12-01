package cn.magic.backgroundmanagement.utils;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.Ipv4Util;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

import java.io.File;
import java.io.IOException;

/**
 * ip2region 工具类（支持 IPv4 + IPv6）
 */
public class Ip2RegionUtil {

    //数据库文件路径
    private static final String DB_V4 = "db/ip2region_v4.xdb";
    private static final String DB_V6 = "db/ip2region_v6.xdb";

    //静态缓存：避免频繁创建searcher
    private static Searcher searcherV4;
    private static Searcher searcherV6;

    static {
        try {
            // 初始化 IPv4 searcher
            File v4 = loadDb(DB_V4);
            searcherV4 = Searcher.newWithFileOnly(Version.IPv4, v4.getAbsolutePath());

            // 初始化 IPv6 searcher
            File v6 = loadDb(DB_V6);
            searcherV6 = Searcher.newWithFileOnly(Version.IPv6, v6.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("初始化 ip2region 出错: " + e.getMessage());
        }
    }

    /**
     * 加载classpath下的数据库文件
     */
    private static File loadDb(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file = new File(Ip2RegionUtil.class.getClassLoader().getResource(path).getFile());
            } catch (Exception ignored) {
                throw new RuntimeException("未找到 IP 数据库文件: " + path);
            }
        }
        return file;
    }

    /**
     * 获取IP归属地 —— 自动识别IPv4/IPv6
     */
    public static String getRegion(String ip) {
        // === 特殊处理 START ===
        if (ip == null || ip.isEmpty()) return "未知";
        ip = ip.trim();
        if (ip.contains("localhost") || "0:0:0:0:0:0:0:1".equals(ip)) return "内网IP";
        // 非 IPv4、IPv6 → 直接返回未知
        if (!Validator.isIpv4(ip) && !Validator.isIpv6(ip)) {
            return "未知";
        }
        // IPv4 内网判断
        if (Validator.isIpv4(ip) && Ipv4Util.isInnerIP(ip)) {
            return "内网IP";
        }
        // === 特殊处理 END ===
        try {
            // ------ IPv4 查询 ------
            if (Validator.isIpv4(ip)) {
                String result = searcherV4.search(ip);
                return extractRegion(result);
            }
            // ------ IPv6 查询 ------
            if (Validator.isIpv6(ip)) {
                String result = searcherV6.search(ip);
                return extractRegion(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }
    /**
     * 格式化查询结果
     */
    private static String extractRegion(String result) {
        if (result == null) return "未知";
        // 标准返回格式：国家|区域|省份|城市|运营商
        String[] split = result.split("\\|");
        if (split.length < 5) return result;
        return split[2] + split[3] + split[4];
    }
    /**
     * 关闭searcher
     */
    public static void close() {
        try {
            if (searcherV4 != null) searcherV4.close();
            if (searcherV6 != null) searcherV6.close();
        } catch (IOException ignored) {}
    }
}


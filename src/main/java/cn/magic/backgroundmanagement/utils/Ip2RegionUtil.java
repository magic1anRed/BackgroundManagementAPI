package cn.magic.backgroundmanagement.utils;


import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

import java.io.File;
import java.io.IOException;

/**
 * ip2region 工具类
 */
public class Ip2RegionUtil {

    // 数据库文件路径，可以放在 resources 下
    private static final String DB_PATH = "db/ip2region_v4.xdb";

    private static Searcher searcher;

    static {
        try {
            // 优先从项目根目录读取
            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                // 如果放在 resources 下，获取绝对路径
                dbFile = new File(Ip2RegionUtil.class.getClassLoader().getResource(DB_PATH).getFile());
            }
            searcher = Searcher.newWithFileOnly(Version.IPv4,dbFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化 ip2region 出错: " + e.getMessage());
        }
    }

    /**
     * 根据 IP 获取归属地
     * @param ip IP 地址
     * @return 归属地信息
     */
    public static String getRegion(String ip) {
        if (ip == null || ip.isEmpty()) {
            return "未知";
        }
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    /**
     * 关闭资源
     */
    public static void close() {
        if (searcher != null) {
            try {
                searcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



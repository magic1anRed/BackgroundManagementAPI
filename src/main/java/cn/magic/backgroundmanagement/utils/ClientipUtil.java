package cn.magic.backgroundmanagement.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.Ipv4Util;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

/**
 * 客户端IP地址工具类
 * @visduo
 */
public class ClientipUtil {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(ClientipUtil.class);

    /**
     * ip2region数据库文件路径
     *
     * ResourceUtil.getResource().getPath()：Solon内置工具类提供的方法，用于获取classpath下的文件的具体路径
     */
    private static final String dbPath = ResourceUtil.getResource("db/ip2region_v4.xdb").getPath();

    /**
     * ip2region版本，设定为IPv4
     */
    private static final Version version = Version.IPv4;

    /**
     * 解析IP地址
     * @visduo
     *
     * @param ip IP信息
     * @return IP地址
     */
    public static String parse(String ip) {
        // 0、特殊IP处理
        if(ip.contains("localhost")) {
            // localhost直接返回内网IP
            return "内网IP";
        } else if(!Validator.isIpv4(ip)) {
            // 判断IPv4地址是否合法，不合法则结束
            // 参考文档：https://plus.hutool.cn/apidocs/cn/hutool/core/lang/Validator.html
            return "未知";
        } else if(Ipv4Util.isInnerIP(ip)) {
            // 其他格式的内网IP由ip2region处理结果比较乱，可以调用HuTool工具类判断是否为内网IP
            // 参考文档：https://plus.hutool.cn/apidocs/cn/hutool/core/net/Ipv4Util.html
            return "内网IP";
        }

        // 参考文档：参考文档：https://gitee.com/lionsoul/ip2region/tree/master/binding/java
        // 1、文件验证
        try {
            Searcher.verifyFromFile(dbPath);
        } catch (Exception e) {
            // 适用性验证失败
            // 当前查询客户端实现不适用于dbPath指定的xdb文件的查询.
            // 应该停止启动服务，使用合适的xdb文件或者升级到适合dbPath的Searcher实现
            logger.error("ip2region数据库文件验证失败：{}", e.getMessage());
            return "未知";
        }

        // 2、从dbPath加载整个xdb到内存
        LongByteArray cBuff;
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            logger.error("ip2region数据库文件加载失败：{}", e.getMessage());
            return "未知";
        }

        // 3、使用上述的cBuff创建一个完全基于内存的查询对象
        Searcher searcher;
        try {
            searcher = Searcher.newWithBuffer(version, cBuff);
        } catch (Exception e) {
            logger.error("ip2region创建查询对象失败：{}", e.getMessage());
            return "未知";
        }

        // 4、查询
        try {
            String searchResult = searcher.search(ip);
            // 查询结果格式：中国|0|福建省|厦门市|电信
            // 只需要提取省市县运营商，格式化字符串提取
            String[] split = searchResult.split("\\|");
            return split[2] + split[3] + split[4];
        } catch (Exception e) {
            logger.error("ip2region查询失败：{}", e.getMessage());
        }

        // 5、关闭资源：该searcher对象可以安全用于并发，等整个服务关闭的时候再关闭searcher
        // searcher.close();

        return "未知";
    }



}
package cn.magic.backgroundmanagement.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

/**
 * MD5盐值加密工具类
 * @visduo
 */
public class MD5SaltsUtil {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(MD5SaltsUtil.class);

    /**
     * 生成随机盐值
     * @visduo
     *
     * @return 随机盐值
     */
    public static String salts() {
        // 调用HuTool工具类生成随机16位长度的字符串
        // 参考文档：https://plus.hutool.cn/apidocs/cn/hutool/core/util/RandomUtil.html#randomString-int-
        return RandomUtil.randomString(16);
    }

    /**
     * MD5盐值加密
     * @visduo
     *
     * @param password 明文密码
     * @param salts 盐值
     * @return 密文密码
     */
    public static String md5(String password, String salts) {
        // 密码和盐值进行MD5加密
        // 参考文档：https://plus.hutool.cn/apidocs/cn/hutool/crypto/SecureUtil.html#md5--
        // 在明文密码的基础上进行撒盐的规则可自定义
        return SecureUtil.md5(salts + password + salts + salts);
    }

    /**
     * 测试方法
     * @visduo
     *
     * @param args main args
     */
    public static void main(String[] args) {
        // 明文密码
        String password = "123456";
        // 普通MD5加密密文密码
        String md5Password = SecureUtil.md5(password);
        // 加盐MD5加密密文密码
        String md5SaltsPassword = md5(password, salts());

        logger.debug("密码{}的普通MD5加密密文密码为：{}", password, md5Password);
        logger.debug("密码{}的加盐MD5加密密文密码为：{}", password, md5SaltsPassword);
    }

}
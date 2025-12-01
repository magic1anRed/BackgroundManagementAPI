package cn.magic.backgroundmanagement.utils;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisx;
import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

/**
 * SaToken配置类
 * @visduo
 */
@Configuration
public class SaTokenConfig {

    /**
     * 构建SaTokenDao Bean
     * @visduo
     *
     * @Inject：注入RedisClient
     * 多种 Redis 接口适配可以复用一份配置
     * 参考文档：https://solon.noear.org/article/592
     *
     * @param client 注入RedisClient
     * @return SaTokenDao
     */
    @Bean
    public SaTokenDao saTokenDao(@Inject RedisClient client){
        return new SaTokenDaoForRedisx(client);
    }

}
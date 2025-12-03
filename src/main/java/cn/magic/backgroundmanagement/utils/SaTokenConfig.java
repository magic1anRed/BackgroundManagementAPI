package cn.magic.backgroundmanagement.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisx;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.solon.integration.SaTokenInterceptor;
import cn.dev33.satoken.stp.StpUtil;
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


    @Bean
    public SaTokenInterceptor saTokenInterceptor() {
        return new SaTokenInterceptor()
                // 设置拦截路径
                .addInclude("/**")
                // 认证函数：每次访问进入
                .setAuth(r -> {
                    // 登录认证
                    SaRouter
                            // 设置需要登录认证的路径
                            .match("/**")
                            // 设置不需要登录认证的路径
                            .notMatch("/system/login")
                            // 认证方式 StpUtil::checkLogin 未登录会报错
                            .check(StpUtil::checkLogin);
                })
                // 异常处理函数
                .setError(e -> {
                    // 判断异常类型
                    if(e instanceof NotLoginException) {
                        // 错误类型是未登录NotLoginException，封装返回401状态
                        return R.error(401,"未登录");
                    } else if(e instanceof NotRoleException || e instanceof NotPermissionException) {
                        // 错误类型是无权限NotRoleException/NotPermissionException，封装返回403状态
                        return R.error("暂无权限");
                    }
                    // 异常类型无法匹配，直接返回异常对象
                    return e;
                })
                // 前置函数：在路由之前调用，配置跨域
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Allow-Headers", "*");
                });
    }
}
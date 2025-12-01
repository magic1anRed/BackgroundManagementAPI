package cn.magic.backgroundmanagement.service;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpUtil;
import cn.magic.backgroundmanagement.entity.LoginlogEntity;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.utils.Ip2RegionUtil;
import cn.magic.backgroundmanagement.utils.MD5SaltsUtil;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;

import java.util.Base64;

@Component
public class LoginService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    @Inject
    private SaTokenDao saTokenDao;

    public R login(Context ctx,String username, String password) {
        System.out.println("username = " + username);
        // 查询用户
        UsersEntity usersEntity = easyEntityQuery.queryable(UsersEntity.class)
                .where(u -> {
                    u.username().eq(username);
                }).firstOrNull();
        // ---- 用户不存在 ----
        if (usersEntity == null) {
            return R.error("用户不存在！登陆失败！");
        }
        // ---- 加盐后验证密码是否正确 ----
        if (!usersEntity.getPassword().equals(MD5SaltsUtil.md5(password, usersEntity.getSalts()))) {
            return R.error("密码错误！登陆失败！");
        }
        // ---- 验证用户状态是否被禁用 ----
        if (usersEntity.getStatus() == 0) {
            return R.error("用户被禁用！登陆失败！");
        }
        // ---- 保存登录日志 ----
        String clientIp = getClientIp(ctx);
        LoginlogEntity loginlogEntity = new LoginlogEntity();
        loginlogEntity.setUserId(usersEntity.getId());
        loginlogEntity.setIp(clientIp);
        loginlogEntity.setAddress(Ip2RegionUtil.getRegion(clientIp));
        loginlogEntity.setTimestamp(System.currentTimeMillis());
        long l = easyEntityQuery.insertable(loginlogEntity).executeRows();
        if (l == 0) {
            return R.error("保存登录日志失败！登陆失败！");
        }
        // ---- 生成saToken并保存到redis 返回给前端
        StpUtil.login(usersEntity.getId());
        String token = StpUtil.getTokenValue();
        saTokenDao.set("test:token", token, 3600);
        return R.ok("登陆成功！~",token);
    }


    public String getClientIp(Context ctx) {
        // 1. 先尝试从 X-Forwarded-For 获取（代理或负载均衡情况）
        String ip = ctx.header("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 2. 再尝试从 X-Real-IP
            ip = ctx.header("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 3. 最后用 remoteAddress
            ip = ctx.realIp(); // Solon 提供的获取远程 IP 方法
        }

        // 如果是多级代理，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}

package cn.magic.backgroundmanagement.service;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpUtil;
import cn.magic.backgroundmanagement.entity.LoginlogEntity;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.entity.proxy.UsersEntityProxy;
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
                })
                .include(UsersEntityProxy::dept)
                .include(UsersEntityProxy::role)
                .firstOrNull();
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
        LoginlogEntity loginlogEntity = new LoginlogEntity();
        loginlogEntity.setUserId(usersEntity.getId());
        loginlogEntity.setIp(ctx.realIp());
        loginlogEntity.setAddress(Ip2RegionUtil.getRegion(ctx.realIp()));
        loginlogEntity.setTimestamp(System.currentTimeMillis());
        long l = easyEntityQuery.insertable(loginlogEntity).executeRows();
        if (l == 0) {
            return R.error("保存登录日志失败！登陆失败！");
        }
        // ---- 生成saToken并保存到redis 返回给前端 ----
        StpUtil.login(usersEntity.getId());
        String token = StpUtil.getTokenValue();
        StpUtil.getSession().set("userInfo", usersEntity);
        return R.ok("登陆成功！~",token);
    }
}

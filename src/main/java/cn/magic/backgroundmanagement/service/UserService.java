package cn.magic.backgroundmanagement.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.entity.proxy.UsersEntityProxy;
import cn.magic.backgroundmanagement.utils.MD5SaltsUtil;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.api.proxy.entity.update.ExpressionUpdatable;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;

@Component
public class UserService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    public R updateUserInfo(Integer id, String username, String realname, String remarks) {

        ExpressionUpdatable<UsersEntityProxy, UsersEntity> upd = easyEntityQuery.updatable(UsersEntity.class)
                .where(u -> u.id().eq(id));

        if (username != null && !username.isEmpty()) {
            upd.setColumns(u -> u.username().set(username));
        }
        if (realname != null && !realname.isEmpty()) {
            upd.setColumns(u -> u.realname().set(realname));
        }
        if (remarks != null && !remarks.isEmpty()) {
            upd.setColumns(u -> u.remarks().set(remarks));
        }
        long rows = upd.executeRows();
        UsersEntity usersEntity = easyEntityQuery.queryable(UsersEntity.class).where(u -> u.id().eq(id)).firstNotNull();
        StpUtil.getSession().set("userInfo",usersEntity).update();
        String token = StpUtil.getTokenValue();
        return rows > 0 ? R.ok("更新成功", token) : R.error("未更新任何数据");
    }

    public R updateUserPassword(Integer id, String oldPassword, String newPassword) {
        //判断旧密码输入是否正确
        UsersEntity usersEntity = easyEntityQuery.queryable(UsersEntity.class)
                .where(u -> u.id().eq(id)).firstOrNull();
        if (usersEntity != null && !usersEntity.getPassword().equals(MD5SaltsUtil.md5(oldPassword, usersEntity.getSalts()))) {
            return R.error("旧密码输入错误！");
        }else {
            //将新密码加密 存入数据库
            System.out.println("newPassword = " + usersEntity.toString());
            String pwd = MD5SaltsUtil.md5(newPassword, usersEntity.getSalts());
            long l = easyEntityQuery.updatable(UsersEntity.class)
                    .where(u -> u.id().eq(id))
                    .setColumns(u -> u.password().set(pwd))
                    .executeRows();
            return l > 0 ? R.ok("密码修改成功！") : R.error("密码修改失败！");
        }
    }
}

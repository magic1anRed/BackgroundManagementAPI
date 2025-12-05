package cn.magic.backgroundmanagement.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.entity.proxy.UsersEntityProxy;
import cn.magic.backgroundmanagement.utils.MD5SaltsUtil;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.api.proxy.entity.update.ExpressionUpdatable;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Param;

@Component
public class UserService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    public R userList(Integer currentPage, Integer pageSize,
                      String username, String realname, String remarks, Integer status) {
        EasyPageResult<UsersEntity> pageResult = easyEntityQuery
                .queryable(UsersEntity.class)
                // 用户名，非空才查询
                .where(username != null && !username.isEmpty(),
                        u -> u.username().like(username))
                // 真实姓名，非空才查询
                .where(realname != null && !realname.isEmpty(),
                        u -> u.realname().like(realname))
                // 备注，非空才查询
                .where(remarks != null && !remarks.isEmpty(),
                        u -> u.remarks().like(remarks))
                // 状态，非空才查询（0 也能查，因为 status=null 才表示不筛选）
                .where(status != null,
                        u -> u.status().eq(status))
                .include(UsersEntityProxy::role)
                .include(UsersEntityProxy::dept)
                .toPageResult(currentPage, pageSize);
        return R.ok("获取用户列表成功！", pageResult);
    }

    public R getUserById(Integer id) {
        UsersEntity usersEntity = easyEntityQuery.queryable(UsersEntity.class)
                .where(u -> u.id().eq(id))
                .include(UsersEntityProxy::role)
                .include(UsersEntityProxy::dept)
                .firstNotNull();
        return R.ok("获取用户信息成功！", usersEntity);
    }

    public R addUser(String username, String realname, String password, String remarks, Integer status,Integer roleId, Integer deptID) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUsername(username);
        usersEntity.setRealname(realname);
        String salts = MD5SaltsUtil.salts();
        usersEntity.setPassword(MD5SaltsUtil.md5(password, salts));
        usersEntity.setSalts(salts);
        usersEntity.setRemarks(remarks);
        usersEntity.setStatus(status);
        usersEntity.setRoleId(roleId);
        usersEntity.setDeptId(deptID);
        long rows = easyEntityQuery.insertable(usersEntity).executeRows();
        return rows > 0 ? R.ok("添加用户成功！") : R.error("添加用户失败！");
    }

    public R updateUser(Integer id, String username, String realname, String password, String remarks, Integer status,Integer roleId, Integer deptID) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(id);
        usersEntity.setUsername(username);
        usersEntity.setRealname(realname);
        usersEntity.setRemarks(remarks);
        usersEntity.setStatus(status);
        long rows;
        if (password != null && !password.isEmpty()) {
            String salts = MD5SaltsUtil.salts();
            usersEntity.setPassword(MD5SaltsUtil.md5(password, salts));
            usersEntity.setSalts(salts);
            usersEntity.setRoleId(roleId);
            usersEntity.setDeptId(deptID);
            rows = easyEntityQuery.updatable(usersEntity)
                    .executeRows();
        }else {
            usersEntity.setRoleId(roleId);
            usersEntity.setDeptId(deptID);
            rows = easyEntityQuery.updatable(usersEntity)
                    .setIgnoreColumns(UsersEntityProxy::salts)
                    .setIgnoreColumns(UsersEntityProxy::password)
                    .executeRows();
        }
        return rows > 0 ? R.ok("更新用户成功！") : R.error("更新用户失败！");
    }

    public R updateUserStatus(Integer id, Integer status) {
        long rows = easyEntityQuery.updatable(UsersEntity.class)
                .where(u -> u.id().eq(id))
                .setColumns(u -> u.status().set(status))
                .executeRows();
        return rows > 0 ? R.ok("更新用户状态成功！") : R.error("更新用户状态失败！");
    }

    public R deleteUser(Integer id) {
        long rows = easyEntityQuery.deletable(UsersEntity.class)
                .where(u -> u.id().eq(id))
                .executeRows();
        return rows > 0 ? R.ok("删除用户成功！") : R.error("删除用户失败！");
    }





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

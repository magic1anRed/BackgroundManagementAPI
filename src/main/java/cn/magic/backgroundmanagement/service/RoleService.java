package cn.magic.backgroundmanagement.service;

import cn.magic.backgroundmanagement.entity.RoleEntity;
import cn.magic.backgroundmanagement.entity.proxy.RoleEntityProxy;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import java.util.List;

@Component
public class RoleService {
    @Db
    private EasyEntityQuery easyEntityQuery;

    public R getRoleList(Integer currentPage, Integer pageSize, String name,String remarks){
        EasyPageResult<RoleEntity> pageResult = easyEntityQuery
                .queryable(RoleEntity.class)
                // 角色名称
                .where(name != null && !name.isEmpty(), r -> r.name().like(name))
                // 权限字符
                .where(remarks != null && !remarks.isEmpty(), r -> r.remarks().like(remarks))
                .toPageResult(currentPage, pageSize);
        return R.ok("获取角色列表成功！",pageResult);
    }

    public R getRoleById(Integer id){
        RoleEntity roleEntity = easyEntityQuery.queryable(RoleEntity.class)
                .where(r -> r.id().eq(id))
                .firstNotNull();
        return R.ok("获取角色成功！",roleEntity);
    }

    public R addRole(String name, String remarks){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(null);
        roleEntity.setName(name);
        roleEntity.setRemarks(remarks);
        long l = easyEntityQuery.insertable(roleEntity).executeRows();
        if (l == 0) {
            return R.error("添加角色失败！");
        }
        return R.ok("添加角色成功！");
    }

    public R updateRole(Integer id, String name,String perms, String remarks){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName(name);
        roleEntity.setPerms(perms);
        roleEntity.setRemarks(remarks);
        long l = easyEntityQuery.updatable(roleEntity).where(r -> r.id().eq(id)).executeRows();
        if (l == 0) {
            return R.error("更新角色失败！");
        }
        return R.ok(name,"更新角色成功！");
    }

    public R deleteRole(Integer id){
        long l = easyEntityQuery.deletable(RoleEntity.class)
                .where(r -> r.id().eq(id))
                .executeRows();
        return l == 0 ? R.error("删除角色失败！") : R.ok("删除角色成功！");
    }

    public R getRoleName(){
        List<RoleEntity> list = easyEntityQuery.queryable(RoleEntity.class)
                .select(r -> r.FETCHER.id().name())
                .toList();
        return R.ok("获取角色名称成功！",list);
    }
}

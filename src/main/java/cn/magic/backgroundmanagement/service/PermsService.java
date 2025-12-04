package cn.magic.backgroundmanagement.service;


import cn.hutool.json.JSONUtil;
import cn.magic.backgroundmanagement.entity.PermsEntity;
import cn.magic.backgroundmanagement.entity.RoleEntity;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.entity.proxy.UsersEntityProxy;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermsService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    public R list(Integer id) {
        // ---- 获取所有菜单列表 ----
        List<PermsEntity> perms = easyEntityQuery.queryable(PermsEntity.class)
                .where(p -> p.status().eq(1))
                .toList();
        // ---- 获取用户权限 ----
        String s = easyEntityQuery.queryable(UsersEntity.class)
                .leftJoin(RoleEntity.class, (user, role) -> user.roleId().eq(role.id()))
                .where(u -> u.id().eq(id))
                .select((user, role) -> role.perms())
                .firstNotNull(String.class);
        // ---- 解析用户权限字符串成 List<Integer> ----
        List<Integer> userPermIds = JSONUtil.toList(s, Integer.class);
        // ---- 过滤菜单，只保留用户有权限的 ----
        List<PermsEntity> filteredPerms = perms.stream()
                .filter(p -> userPermIds.contains(p.getId()))
                .collect(Collectors.toList());
            // ---- 打印调试 ----
        System.out.println("filteredPerms = " + filteredPerms);
        // ---- 返回给前端 ----
        return R.ok("获取菜单成功", filteredPerms);
    }


    public R treeList(Integer userId) {
        // 1. 获取所有启用的菜单
        List<PermsEntity> perms = easyEntityQuery.queryable(PermsEntity.class)
                .where(p -> p.status().eq(1))
                .toList();

        // 2. 获取用户权限字符串
        String s = easyEntityQuery.queryable(UsersEntity.class)
                .leftJoin(RoleEntity.class, (user, role) -> user.roleId().eq(role.id()))
                .where(u -> u.id().eq(userId))
                .select((user, role) -> role.perms())
                .firstNotNull(String.class);

        // 3. 解析为 List<Integer>
        List<Integer> userPermIds = JSONUtil.toList(s, Integer.class);

        // 4. 过滤用户有权限的菜单
        List<PermsEntity> filteredPerms = perms.stream()
                .filter(p -> userPermIds.contains(p.getId()))
                .collect(Collectors.toList());

        // 5. 构建树形菜单
        List<PermsEntity> tree = buildTree(filteredPerms, 0);

        return R.ok("获取菜单成功", tree);
    }


    /**
     * 构建树形菜单
     */
    private List<PermsEntity> buildTree(List<PermsEntity> list, Integer parentId) {
        List<PermsEntity> tree = new ArrayList<>();
        for (PermsEntity perm : list) {
            if (perm.getParentId().equals(parentId)) {

                // 递归查找子菜单
                List<PermsEntity> children = buildTree(list, perm.getId());
                if (!children.isEmpty()) {
                    perm.setChildren(children);
                }

                tree.add(perm);
            }
        }
        return tree;
    }


}

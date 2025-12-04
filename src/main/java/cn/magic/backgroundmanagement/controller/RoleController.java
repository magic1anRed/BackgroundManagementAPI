package cn.magic.backgroundmanagement.controller;

import cn.magic.backgroundmanagement.service.RoleService;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;

@Controller
@Mapping("/system/role")
public class RoleController {

    @Inject
    private RoleService roleService;

    @Get
    @Mapping("/getRoleList")
    public R getRoleList(@Param("currentPage") Integer currentPage,
                         @Param("pageSize") Integer pageSize,
                         @Param("name") String name,
                         @Param("remarks") String remarks) {
        return roleService.getRoleList(currentPage, pageSize, name, remarks);
    }

    @Get
    @Mapping("/getRoleById")
    public R getRoleById(@Param("id") Integer id) {
        return roleService.getRoleById(id);
    }

    @Get
    @Mapping("/getRoleName")
    public R getRoleName() {
        return roleService.getRoleName();
    }

    @Post
    @Mapping("/addRole")
    public R addRole(@Param("name") String name,
                     @Param("remarks") String remarks) {
        return roleService.addRole(name, remarks);
    }

    @Put
    @Mapping("/updateRole")
    public R updateRole(@Param("id") Integer id,
                       @Param("name") String name,
                       @Param("perms") String perms,
                       @Param("remarks") String remarks) {
        return roleService.updateRole(id, name, perms,remarks);
    }

    @Delete
    @Mapping("/deleteRole")
    public R deleteRole(@Param("id") Integer id) {
        return roleService.deleteRole(id);
    }


}

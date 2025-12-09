package cn.magic.backgroundmanagement.controller;

import cn.magic.backgroundmanagement.entity.PermsEntity;
import cn.magic.backgroundmanagement.service.PermsService;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;

@Controller
@Mapping("/system/perms")
public class PermsController {

    @Inject
    private PermsService permsService;

    @Get
    @Mapping("/getPermsList")
    public R getPermsList(@Param("userId") Integer userId) {
        return permsService.treeList(userId);
    }

    @Get
    @Mapping("/getPerms")
    public R getPerms(String name,Integer status) {
        return permsService.list(name,status);
    }

    @Get
    @Mapping("/list")
    public R getPermsById() {
        return permsService.getPerms();
    }

    @Put
    @Mapping("/updatePerms")
    public R updatePerms(PermsEntity permsEntity) {
        return permsService.update(permsEntity);
    }

    @Post
    @Mapping("/addPerms")
    public R addPerms(PermsEntity permsEntity) {
        return permsService.add(permsEntity);
    }

    @Delete
    @Mapping("/deletePerms/{id}")
    public R deletePerms(@Param Integer id) {
        return permsService.delete(id);
    }
}

package cn.magic.backgroundmanagement.controller;

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
}

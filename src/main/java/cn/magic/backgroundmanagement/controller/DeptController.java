package cn.magic.backgroundmanagement.controller;

import cn.magic.backgroundmanagement.service.DeptService;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;

@Controller
@Mapping("/system/dept")
public class DeptController {

    @Inject
    private DeptService deptService;

    @Get
    @Mapping("/getDeptList")
    public R getDeptList(@Param("currentPage") Integer currentPage,
                         @Param("pageSize") Integer pageSize,
                         @Param("name") String name) {
        return R.ok("查询成功", deptService.deptList(currentPage, pageSize, name));
    }

    @Post
    @Mapping("/addDept")
    public R addDept(@Param("name") String name) {
        return R.ok(name,deptService.addDept(name));
    }

    @Put
    @Mapping("/updateDept")
    public R updateDept(@Param("id") Integer id,
                        @Param("name") String name) {
        return R.ok(deptService.updateDept(id, name));
    }

    @Delete
    @Mapping("/deleteDept")
    public R deleteDept(@Param("id") Integer id) {
        return R.ok(deptService.deleteDept(id));
    }
}

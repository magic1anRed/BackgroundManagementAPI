package cn.magic.backgroundmanagement.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.service.LoginService;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.Valid;

@Valid
@Controller
@Mapping("/system")
public class LoginController {

    @Inject
    private LoginService loginService;

    @Post
    @Mapping("/login")
    public R login(Context ctx,
                   @Param("username") @NotBlank(message = "用户名不能为空") String username,
                   @Param("password") @NotBlank(message = "密码不能为空") String password) {
        return loginService.login(ctx, username, password);
    }

    @Post
    @Mapping("/logout")
    public R logout() {
        StpUtil.logout();
        return R.ok("注销成功！");
    }

    @Get
    @Mapping("/getInfo")
    public R getInfo() {
        UsersEntity userInf  = (UsersEntity) StpUtil.getSession().get("userInfo");
        return R.ok("获取用户信息成功！",userInf);
    }
}

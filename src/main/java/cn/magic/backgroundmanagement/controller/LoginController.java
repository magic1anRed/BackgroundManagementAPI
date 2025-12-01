package cn.magic.backgroundmanagement.controller;


import cn.magic.backgroundmanagement.service.LoginService;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;


@Controller
@Mapping("/system")
public class LoginController {

    @Inject
    private LoginService loginService;

    @Post
    @Mapping("/login")
    public R login(Context ctx,
                   @Param("username") String username,
                   @Param("password") String password) {
        return loginService.login(ctx, username, password);
    }
}

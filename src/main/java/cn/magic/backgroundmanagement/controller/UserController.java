package cn.magic.backgroundmanagement.controller;

import cn.magic.backgroundmanagement.service.UserService;
import cn.magic.backgroundmanagement.utils.QiniuUtils;
import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.UploadedFile;

@Controller
@Mapping("/system/user")
public class UserController {

    @Inject
    private UserService userService;
    @Inject
    private QiniuUtils qiniuUtils;

    @Put
    @Mapping("/updateUserInfo")
    public R updateUserInfo(
            @Param("id") Integer id,
            @Param("username") String username,
            @Param("realname") String realname,
            @Param("remarks") String remarks
            ){
        return userService.updateUserInfo(id, username, realname, remarks);
    }

    @Put
    @Mapping("/updateUserPassword")
    public R updateUserPassword(
            @Param("id") Integer id,
            @Param("oldPassword") String oldPassword,
            @Param("newPassword") String newPassword
    ){
        return userService.updateUserPassword(id, oldPassword, newPassword);
    }

    @Post
    @Mapping("/uploadAvatar")
    public R uploadAvatar(UploadedFile file){
        String url = qiniuUtils.upload(file);
        return R.ok(url);
    }

}

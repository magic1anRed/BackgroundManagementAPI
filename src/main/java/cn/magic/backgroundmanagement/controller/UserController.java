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

    @Get
    @Mapping("/userList")
    public R userList(
            Integer currentPage,
            Integer pageSize,
            String username,
            String realname,
            String remarks,
            Integer status
    ){
        return userService.userList(currentPage, pageSize, username, realname, remarks, status);
    }

    @Get
    @Mapping("/getUserById")
    public R getUserById(@Param("id") Integer id){
        return userService.getUserById(id);
    }

    @Post
    @Mapping("/addUser")
    public R addUser(
            @Param("username") String username,
            @Param("realname") String realname,
            @Param("password") String password,
            @Param("remarks") String remarks,
            @Param("status") Integer status,
            @Param("roleId") Integer roleId,
            @Param("deptID") Integer deptID
    ){
        return userService.addUser(username, realname, password, remarks, status, roleId, deptID);
    }

    @Put
    @Mapping("/updateUser")
    public R updateUser(
             Integer id,
             String username,
             String realname,
             String password,
             String remarks,
             Integer status,
             Integer roleId,
             Integer deptID
    ){
        return userService.updateUser(id, username, realname, password, remarks, status, roleId, deptID);
    }

    @Put
    @Mapping("/updateUserStatus")
    public R updateUserStatus(
            @Param("id") Integer id,
            @Param("status") Integer status
    ){
        return userService.updateUserStatus(id,status);
    }

    @Delete
    @Mapping("/deleteUser")
    public R deleteUser(@Param("id") Integer id){
        return userService.deleteUser( id);
    }



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
    public R uploadAvatar(Integer id,UploadedFile file){
        return userService.uploadUserAvatar(id,file);
    }

}

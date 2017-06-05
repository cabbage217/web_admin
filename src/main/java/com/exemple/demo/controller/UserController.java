package com.exemple.demo.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.IUserService;
import com.exemple.demo.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Caby on 2017-05-28 1:35 PM.
 */

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * 分页获取用户列表
     * @param pageNo Integer, 分页数字，从0开始
     * @return Result
     */
    @GetMapping("/list")
    public Result userList(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("userCount", userService.userCount());
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", SqlParam.PageSize);
        resultMap.put("userList", userService.userList(pageNo));
        return new Result<>(ResultEnum.SUCCESS, resultMap);
    }

    /**
     * 添加用户
     * @param username String, 用户名
     * @param password String, 密码, sha1(md5(input) + salt)
     * @param status Integer, 状态
     * @return
     */
    @PutMapping("/add")
    public Result addUser(@RequestParam("username") String username, @RequestParam("pwd") String password,
                          @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return new Result(
                userService.addUser(username, password, status) > 0
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 删除用户
     * @param body String, 请求体, RequestParam注解不起作用
     * @param request HttpServletRequest
     * @return Result
     */
    @DeleteMapping("/del")
    public Result deleteUser(@RequestBody String body, HttpServletRequest request) { // RequestParam does not work!
        List<String> list = Splitter.on("=").splitToList(body);
        Integer uid = Integer.parseInt(list.get(1));
        Integer myUid = SessionUtil.getUidWithRequest(request);
        if (uid.equals(myUid)) {
            return new Result(ResultEnum.ERR_CAN_NOT_DEL_SELF);
        }
        return new Result(
                userService.deleteUser(uid)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 修改用户信息
     * @param uid Integer, 用户id
     * @param username String, 新的用户名
     * @param password String, 新密码, 为空则不改变
     * @param status Integer, 状态
     * @return Result
     */
    @PutMapping("/update") // no update method
    public Result updateUser(@RequestParam("uid") Integer uid, @RequestParam("username") String username,
                             @RequestParam("pwd") String password,
                             @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {

        return new Result(
                userService.updateUser(uid, username, password, status)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 设置用户角色
     * @param uid Integer, 用户id
     * @param roleIds List<Integer>, 角色id列表
     * @return Result
     */
    @PutMapping("/setRoles")
    public Result setRoles(@RequestParam("uid") Integer uid,
                           @RequestParam(value = "roleIds[]", required = false) List<Integer> roleIds) {
        return new Result(
                userService.setRoles(uid, roleIds)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 获取自己的信息
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/selfInfo")
    public Result selfInfo(HttpServletRequest request) {
        return new Result<>(ResultEnum.SUCCESS, userService.userInfo(SessionUtil.getUidWithRequest(request)));
    }

    /**
     * 自己修改密码
     * @param password String, 新密码, sha1(md5(input) + salt)
     * @param oldPassword String, 原密码, sha1(md5(input) + salt)
     * @param request HttpServletRequest
     * @return Result
     */
    @PutMapping("/resetPwd")
    public Result resetPassword(@RequestParam("pwd") String password, @RequestParam("oldPwd") String oldPassword,
                                HttpServletRequest request) {
        return new Result<>(
                ResultEnum.SUCCESS,
                userService.resetPassword(SessionUtil.getUidWithRequest(request), password, oldPassword)
        );
    }
}

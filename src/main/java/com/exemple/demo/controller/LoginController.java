package com.exemple.demo.controller;

import com.exemple.demo.Exception.MainException;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.ILoginService;
import com.exemple.demo.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Caby on 2017-05-23 7:46 PM.
 */

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private ILoginService loginService;

    /**
     * 登录
     * @param username String, 用户名
     * @param password String, 密码md5
     * @return Result，详见类com.exemple.demo.domain.Result
     */
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password, HttpServletRequest request) {
        Integer uid = loginService.login(username, password);
        if (uid > 0) {
            if (!SessionUtil.createSession(uid, request)) {
                throw new MainException(ResultEnum.ERR_UNKNOWN);
            }
        }
        return new Result<>(
                uid > 0 ? ResultEnum.SUCCESS : ResultEnum.ERR_INVALID_PW,
                "username = " + username + ", password = " + password
        );
    }

    /**
     * 登出
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        logger.info("user logout, uid = {}", SessionUtil.getUidWithRequest(request));
        return new Result(SessionUtil.resetSession(request) ? ResultEnum.SUCCESS : ResultEnum.ERR_UNKNOWN);
    }
}
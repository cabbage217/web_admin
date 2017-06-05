package com.exemple.demo.sevice;

/**
 * Created by Caby on 5/14/17.
 */

public interface ILoginService {
    /**
     * 登录操作
     * @param username String, 用户名
     * @param pwd String, 密码MD5值
     * @return Integer, 登录成功返回用户id，其他返回-1
     */
    Integer login(String username, String pwd);
}

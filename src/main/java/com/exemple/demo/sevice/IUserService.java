package com.exemple.demo.sevice;

import com.exemple.demo.domain.User;

import java.util.List;

/**
 * Created by Caby on 2017-05-28 1:44 PM.
 */

public interface IUserService {
    /**
     * 获取全部用户列表
     * @return List<User>
     */
    List<User> allUsers();

    /**
     * 分页所有用户列表
     * @param pageNo Integer, 页数
     * @return List<User>
     */
    List<User> userList(Integer pageNo);

    /**
     * 所有用户数量
     * @return Integer
     */
    Integer userCount();

    /**
     * 添加用户
     * @param username String, 用户名
     * @param password String, 密码
     * @param status Integer, 状态
     * @return Integer
     */
    Integer addUser(String username, String password, Integer status);

    /**
     * 更新用户
     * @param uid Integer, 用户id
     * @param username String, 用户名
     * @param password String, 新密码
     * @param status Integer, 状态
     * @return boolean
     */
    boolean updateUser(Integer uid, String username, String password, Integer status);

    /**
     * 删除用户
     * @param uid Integer, 用户id
     * @return boolean
     */
    boolean deleteUser(Integer uid);

    /**
     * 重置密码
     * @param uid Integer, 用户id
     * @param password String, 新密码
     * @param oldPassword String, 旧密码
     * @return
     */
    boolean resetPassword(Integer uid, String password, String oldPassword);

    /**
     * 设置角色
     * @param uid Integer, 用户id
     * @param roleIds List<Integer>, 角色id列表
     * @return
     */
    boolean setRoles(Integer uid, List<Integer> roleIds);

    /**
     * 获取某个用户的信息
     * @param uid Integer, 用户id
     * @return
     */
    User userInfo(Integer uid);
}

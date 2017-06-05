package com.exemple.demo.mapper;

import com.exemple.demo.DAOProvider.MainDAOProvider;
import com.exemple.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("SELECT u.id, u.username, u.password, u.status, u.ctime createTime, GROUP_CONCAT(r.`id`) roleIds " +
            "FROM user u " +
            "LEFT JOIN user_role ur ON ur.uid = u.id " +
            "LEFT JOIN role r ON ur.rid = r.id " +
            "GROUP BY u.id")
    List<User> allUsers();

    @Select("SELECT u.id, u.username, u.password, u.status, u.ctime createTime, GROUP_CONCAT(r.`id`) roleIds " +
            "FROM user u " +
            "LEFT JOIN user_role ur ON ur.uid = u.id " +
            "LEFT JOIN role r ON ur.rid = r.id " +
            "GROUP BY u.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<User> userList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("SELECT COUNT(id) FROM user")
    Integer userCount();

    @Select("SELECT id, username, password, status, ctime createTime, '' roleNames " +
            "FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("SELECT id, username, password, status, ctime createTime, '' roleNames " +
            "FROM `user` WHERE id = #{uid}")
    User findUserByUid(@Param("uid") Integer uid);

    @Insert("INSERT INTO user (username, password, `status`) VALUES (#{username}, #{password}, #{status})")
    Integer addUser (@Param("username") String username, @Param("password") String password,
                     @Param("status") Integer status);

    @Update("UPDATE `user` SET `username` = #{username}, password = #{password}, `status` = #{status} WHERE id = #{uid}")
    void updateUser(@Param("uid") Integer uid, @Param("username") String username, @Param("password") String password,
                    @Param("status") Integer status);

    @Delete("DELETE FROM `user` WHERE id = #{uid}")
    void deleteUser(@Param("uid") Integer uid);

    @Update("UPDATE `user` SET `password` = #{password} WHERE id = #{uid}")
    void resetPassword(@Param("uid") Integer uid, @Param("password") String password);

    @Delete("DELETE FROM `user_role` WHERE uid = #{uid}")
    void removeAllRoles(@Param("uid") Integer uid);

    @InsertProvider(type = MainDAOProvider.class, method = "addRolesForUser")
    void setRoles(Integer uid, List<Integer> roleIds);
}
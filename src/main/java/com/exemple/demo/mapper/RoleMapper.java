package com.exemple.demo.mapper;

import com.exemple.demo.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Caby on 2017-05-31 7:21 AM.
 */
public interface RoleMapper {
    @Select("select count(id) from role")
    Integer roleCount();

    @Select("SELECT r.id, r.name, r.des, r.status, r.ctime createTime, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM role r " +
            "LEFT JOIN role_interface ri ON r.id = ri.rid " +
            "LEFT JOIN interface i ON ri.iid = i.id " +
            "GROUP BY r.id " +
            "ORDER BY r.id")
    List<Role> allRoles();

    @Select("SELECT r.id, r.name, r.des, r.status, r.ctime createTime, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM role r " +
            "LEFT JOIN role_interface ri ON r.id = ri.rid " +
            "LEFT JOIN interface i ON ri.iid = i.id " +
            "GROUP BY r.id " +
            "ORDER BY r.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<Role> roleList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("SELECT r.id, r.name, r.des, r.status, r.ctime createTime, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM role r " +
            "LEFT JOIN role_interface ri ON r.id = ri.rid " +
            "LEFT JOIN interface i ON ri.iid = i.id " +
            "WHERE r.status = 0 " +
            "GROUP BY r.id " +
            "ORDER BY r.id")
    List<Role> rolesEnabled();

    @Select("SELECT r.id, r.name, r.des, r.status, r.ctime createTime, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM role r " +
            "LEFT JOIN role_interface ri ON r.id = ri.rid " +
            "LEFT JOIN interface i ON ri.iid = i.id " +
            "WHERE ri.iid = #{iid} " +
            "GROUP BY r.id " +
            "ORDER BY ri.rid, i.order_num DESC")
    List<Role> rolesInterfaceUsing(@Param("iid") Integer iid);

    @Insert("INSERT INTO role (`name`, `des`, `status`) VALUES (#{name}, #{des}, #{status})")
    Integer addRole(@Param("name") String name, @Param("des") String des, @Param("status") Integer status);

    @Update("UPDATE role SET `name` = #{name}, `des` = #{des}, `status` = #{status} WHERE id = #{rid}")
    void updateRole(@Param("rid")Integer rid, @Param("name") String name,
                    @Param("des") String des, @Param("status") Integer status);

    @Delete("DELETE FROM role WHERE id = #{rid}")
    void deleteRole(@Param("rid")Integer rid);
}
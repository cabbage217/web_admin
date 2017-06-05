package com.exemple.demo.sevice;

import com.exemple.demo.domain.Role;

import java.util.List;

/**
 * Created by Caby on 2017-05-31 7:20 AM.
 */

public interface IRoleService {
    /**
     * 所有角色的数量
     * @return Integer
     */
    Integer roleCount();

    /**
     * 获取全部角色列表
     * @return List<Role>
     */
    List<Role> allRoles();

    /**
     * 分页获取所有角色列表
     * @param pageNo Integer, 分页数
     * @return List<Role>
     */
    List<Role> roleList(Integer pageNo);

    /**
     * 获取已启用的角色列表
     * @return List<Role>
     */
    List<Role> rolesEnabled();

    /**
     * 获取接口所属的角色列表
     * @param iid Integer, 接口id
     * @return List<Role>
     */
    List<Role> rolesInterfaceUsing(Integer iid);

    /**
     * 添加角色
     * @param name String, 角色名
     * @param des String, 描述
     * @param status Integer, 状态
     * @return Integer
     */
    Integer addRole(String name, String des, Integer status);

    /**
     * 更新角色
     * @param rid Integer, 角色id
     * @param name name String, 角色名
     * @param des String, 描述
     * @param status Integer, 状态
     * @return boolean
     */
    boolean updateRole(Integer rid, String name, String des, Integer status);

    /**
     * 删除角色
     * @param rid Integer, 角色id
     * @return boolean
     */
    boolean deleteRole(Integer rid);
}

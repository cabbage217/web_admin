package com.exemple.demo.DAOProvider;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Caby on 2017-06-02 9:27 AM.
 */
public class MainDAOProvider {
    /**
     * 为接口添加角色
     * @param iid Integer, 接口id
     * @param roleIds List<Integer>, 角色id列表
     * @return String
     */
    public String addRolesForInterface(Integer iid, List<Integer> roleIds) {
        List<String> values = Lists.newArrayList();
        for (Integer roleId : roleIds) {
            values.add("(" + iid + "," + roleId + ")");
        }
        return (new StringBuilder()).append("INSERT INTO role_interface (iid,rid) VALUES ")
                .append(Joiner.on(",").join(values))
                .toString();
    }

    /**
     * 为用户添加角色
     * @param uid Integer, 用户id
     * @param roleIds List<Integer>, 角色id列表
     * @return String
     */
    public String addRolesForUser(Integer uid, List<Integer> roleIds) {
        List<String> values = Lists.newArrayList();
        for (Integer roleId : roleIds) {
            values.add("(" + uid + "," + roleId + ")");
        }
        return (new StringBuilder()).append("INSERT INTO user_role (uid,rid) VALUES ")
                .append(Joiner.on(",").join(values))
                .toString();
    }
}

package com.exemple.demo.sevice;

import com.exemple.demo.domain.Interface;
import com.exemple.demo.domain.Navigation;

import java.util.List;

/**
 * Created by Caby on 2017-05-27 10:35 AM.
 */
public interface IInterfaceService {
    /**
     * 接口总数量
     * @return Integer
     */
    Integer interfaceCount();

    /**
     * 获取所有接口列表
     * @return List<Interface>
     */
    List<Interface> allInterfaces();

    /**
     * 分页获取所有接口
     * @param pageNo Integer, 分页数
     * @return List<Interface>
     */
    List<Interface> interfaceList(Integer pageNo);

    /**
     * 添加接口
     * @param name String, 接口名称
     * @param url String, url
     * @param type Integer, 接口类型, 0: API, 1: 页面
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return boolean
     */
    boolean addInterface(String name, String url, Integer type, Integer orderNumber, Integer status);

    /**
     * 删除接口
     * @param iid Integer, 接口id
     * @return boolean
     */
    boolean deleteInterface(Integer iid);

    /**
     * 更新接口
     * @param iid Integer, 接口id
     * @param name String, 接口名称
     * @param url String, url
     * @param type Integer, 接口类型, 0: API, 1: 页面
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return boolean
     */
    boolean updateInterface(Integer iid, String name, String url, Integer type, Integer orderNumber, Integer status);

    /**
     * 设置导航栏
     * @param iid Integer, 接口id
     * @param nid Integer, 导航栏id
     * @return boolean
     */
    boolean setNavigation(Integer iid, Integer nid);

    /**
     * 设置角色
     * @param iid Integer, 接口id
     * @param roleIds List<Integer>，角色id列表
     * @return boolean
     */
    boolean setRoles(Integer iid, List<Integer> roleIds);

    /**
     * 用户是否有权限使用接口
     * @param uid Integer, 用户id
     * @param uri String, 接口地址
     * @return boolean
     */
    boolean isUserHasPermissionToInterface(Integer uid, String uri);
}

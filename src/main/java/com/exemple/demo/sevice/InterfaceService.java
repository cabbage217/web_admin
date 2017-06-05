package com.exemple.demo.sevice;

import com.google.common.collect.Lists;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Interface;
import com.exemple.demo.mapper.InterfaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Caby on 2017-05-27 10:37 AM.
 */

@Service
public class InterfaceService implements IInterfaceService {
    private Logger logger = LoggerFactory.getLogger(InterfaceService.class);

    @Autowired
    private InterfaceMapper interfaceMapper;

    private Interface transform(com.exemple.demo.entity.Interface oneInterface) {
        if (oneInterface == null) {
            return null;
        }
        Interface one = new Interface();
        one.setId(oneInterface.getId());
        one.setName(oneInterface.getName());
        one.setUrl(oneInterface.getUrl());
        one.setStatus(oneInterface.getStatus());
        one.setType(oneInterface.getType());
        one.setCreateTime(oneInterface.getCreateTime());
        one.setOrderNumber(oneInterface.getOrderNumber());
        one.setNid(oneInterface.getNid());
        one.setNavigationName(oneInterface.getNavigationName());
        return one;
    }

    private List<Interface> transform(List<com.exemple.demo.entity.Interface> interfaceList) {
        List<Interface> resultList = Lists.newArrayList();
        if (interfaceList == null || interfaceList.isEmpty()) {
            return resultList;
        }
        for (com.exemple.demo.entity.Interface oneInterface : interfaceList) {
            Interface one = this.transform(oneInterface);
            if (one != null) {
                resultList.add(one);
            }
        }
        return resultList;
    }

    /**
     * 获取接口总数量
     * @return Integer
     */
    @Override
    public Integer interfaceCount() {
        return interfaceMapper.interfaceCount();
    }

    /**
     * 获取全部接口列表
     * @return List<Interface>
     */
    @Override
    public List<Interface> allInterfaces() {
        return this.transform(interfaceMapper.allInterfaces());
    }

    /**
     * 分页获取接口列表
     * @param pageNo Integer, 分页数
     * @return List<Interface>
     */
    @Override
    public List<Interface> interfaceList(Integer pageNo) {
        if (pageNo == null) {
            pageNo = 0;
        }
        return this.transform(interfaceMapper.interfaceList(pageNo * SqlParam.PageSize, SqlParam.PageSize));
    }

    /**
     * 添加接口
     * @param name String, 接口名称
     * @param url String, url
     * @param type Integer, 接口类型, 0: API, 1: 页面
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return boolean
     */
    @Override
    public boolean addInterface(String name, String url, Integer type, Integer orderNumber, Integer status) {
        return interfaceMapper.addInterface(name, url, type, orderNumber, status) > 0;
    }

    /**
     * 删除接口
     * @param iid Integer, 接口id
     * @return boolean
     */
    @Override
    public boolean deleteInterface(Integer iid) {
        interfaceMapper.deleteInterface(iid);
        return true;
    }

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
    @Override
    public boolean updateInterface(Integer iid, String name, String url, Integer type, Integer orderNumber, Integer status) {
        interfaceMapper.updateInterface(iid, name, url, type, orderNumber, status);
        return true;
    }

    /**
     * 设置导航栏
     * @param iid Integer, 接口id
     * @param nid Integer, 导航栏id
     * @return boolean
     */
    @Override
    public boolean setNavigation(Integer iid, Integer nid) {
        if (nid == 0) {
            nid = null;
        }
        interfaceMapper.setNavigation(iid, nid);
        return true;
    }

    /**
     * 设置角色
     * @param iid Integer, 接口id
     * @param roleIds List<Integer>，角色id列表
     * @return boolean
     */
    @Override
    public boolean setRoles(Integer iid, List<Integer> roleIds) {
        interfaceMapper.deleteAllInterfaceRoles(iid);
        if (roleIds != null && !roleIds.isEmpty()) {
            interfaceMapper.addRolesForInterface(iid, roleIds);
        }
        return true;
    }

    /**
     * 用户是否有权限使用接口
     * @param uid Integer, 用户id
     * @param uri String, 接口地址
     * @return boolean
     */
    @Override
    public boolean isUserHasPermissionToInterface(Integer uid, String uri) {
        return interfaceMapper.isUserHasPermissionToInterface(uid, uri) > 0;
    }
}

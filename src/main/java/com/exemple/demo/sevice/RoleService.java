package com.exemple.demo.sevice;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Interface;
import com.exemple.demo.domain.Role;
import com.exemple.demo.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Caby on 2017-05-31 7:32 AM.
 */

@Service
public class RoleService implements IRoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IInterfaceService interfaceService;
    @Autowired
    private IUserService userService;

    private Interface findInterfaceByIid(Integer iid, List<Interface> interfaceList) {
        if (interfaceList == null || interfaceList.isEmpty() || iid == null) {
            return null;
        }
        for (Interface oneInterface : interfaceList) {
            if (iid.equals(oneInterface.getId())) {
                return oneInterface;
            }
        }
        return null;
    }

    private List<Role> transform(List<com.exemple.demo.entity.Role> roles) {
        List<Role> roleList = Lists.newArrayList();
        if (roles == null || roles.isEmpty()) {
            return roleList;
        }
        List<Interface> allInterfaces = interfaceService.allInterfaces();
        for (com.exemple.demo.entity.Role role : roles) {
            Role one = new Role();
            one.setId(role.getId());
            one.setName(role.getName());
            one.setDes(role.getDes());
            one.setStatus(role.getStatus());
            one.setCreateTime(role.getCreateTime());
            if (one.getInterfaceList() == null) {
                one.setInterfaceList(Lists.newArrayList());
            }
            if (!Strings.isNullOrEmpty(role.getInterfaceIds())) {
                List<String> interfaceIds = Splitter.on(",")
                        .trimResults()
                        .omitEmptyStrings()
                        .splitToList(role.getInterfaceIds());
                for (String idString : interfaceIds) {
                    Integer iid = Integer.parseInt(idString);
                    if (this.findInterfaceByIid(iid, one.getInterfaceList()) == null) {
                        Interface oneInterface = this.findInterfaceByIid(iid, allInterfaces);
                        if (oneInterface != null) {
                            one.getInterfaceList().add(oneInterface);
                        }
                    }
                }
            }

            roleList.add(one);
        }

        return roleList;
    }

    @Override
    public Integer roleCount() {
        return roleMapper.roleCount();
    }

    @Override
    public List<Role> allRoles() {
        return this.transform(roleMapper.allRoles());
    }

    @Override
    public List<Role> roleList(Integer pageNo) {
        if (pageNo == 0) {
            pageNo = 0;
        }
        return this.transform(roleMapper.roleList(pageNo * SqlParam.PageSize, SqlParam.PageSize));
    }

    @Override
    public List<Role> rolesEnabled() {
        return this.transform(roleMapper.rolesEnabled());
    }

    @Override
    public List<Role> rolesInterfaceUsing(Integer iid) {
        return this.transform(roleMapper.rolesInterfaceUsing(iid));
    }

    @Override
    public Integer addRole(String name, String des, Integer status) {
        return roleMapper.addRole(name, des, status);
    }

    @Override
    public boolean updateRole(Integer rid, String name, String des, Integer status) {
        roleMapper.updateRole(rid, name, des, status);
        return true;
    }

    @Override
    public boolean deleteRole(Integer rid) {
        roleMapper.deleteRole(rid);
        return true;
    }
}

package com.exemple.demo.sevice;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.Exception.MainException;
import com.exemple.demo.domain.Role;
import com.exemple.demo.domain.User;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.mapper.UserMapper;
import com.exemple.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Caby on 2017-05-28 2:14 PM.
 */

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IRoleService roleService;

    private Role findRoleByRid(Integer rid, List<Role> roleList) {
        if (roleList == null || roleList.isEmpty() || rid == null) {
            return null;
        }
        for (Role role : roleList) {
            if (role.getId().equals(rid)) {
                return role;
            }
        }
        return null;
    }

    private User transform(com.exemple.demo.entity.User user, List<Role> allRoles) {
        if (user == null) {
            return null;
        }
        User domainUser = new User();
        domainUser.setId(user.getId());
        domainUser.setUsername(user.getUsername());
        domainUser.setStatus(user.getStatus());
        domainUser.setCreateTime(user.getCreateTime());

        domainUser.setRoles(Lists.newArrayList());
        if (!Strings.isNullOrEmpty(user.getRoleIds())) {
            if (allRoles == null) {
                allRoles = roleService.allRoles();
            }
            List<String> roleIds = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(user.getRoleIds());
            for (String idString : roleIds) {
                Integer rid = Integer.parseInt(idString);
                if (this.findRoleByRid(rid, domainUser.getRoles()) == null) {
                    Role role = this.findRoleByRid(rid, allRoles);
                    if (role != null) {
                        domainUser.getRoles().add(role);
                    }
                }
            }
        }

        return domainUser;
    }

    private List<User> transform(List<com.exemple.demo.entity.User> userList) {
        List<User> resultList = Lists.newArrayList();
        if (userList == null || userList.isEmpty()) {
            return resultList;
        }
        List<Role> allRoles = roleService.allRoles();
        for (com.exemple.demo.entity.User oneUser : userList) {
            resultList.add(this.transform(oneUser, allRoles));
        }
        return resultList;
    }

    @Override
    public List<User> allUsers() {
        return this.transform(userMapper.allUsers());
    }

    @Override
    public List<User> userList(Integer pageNo) {
        if (pageNo == 0) {
            pageNo = 0;
        }
        return this.transform(userMapper.userList(pageNo * SqlParam.PageSize, SqlParam.PageSize));
    }

    @Override
    public Integer userCount() {
        return userMapper.userCount();
    }

    @Override
    public Integer addUser(String username, String password, Integer status) {
        if (userMapper.findUserByUsername(username) != null) {
            throw new MainException(ResultEnum.ERR_ADD_URSR_FAIL);
        }
        return userMapper.addUser(username, CommonUtils.sha1(Lists.newArrayList(password)), status);
    }

    @Override
    public boolean updateUser(Integer uid, String username, String password, Integer status) {
        com.exemple.demo.entity.User user = userMapper.findUserByUid(uid);
        if (user == null) {
            throw new MainException(ResultEnum.ERR_CAN_NOT_FIND_USER);
        }
        if (!user.getUsername().equals(username) && userMapper.findUserByUsername(username) != null) {
            throw new MainException(ResultEnum.ERR_ADD_URSR_FAIL);
        }
        if (Strings.isNullOrEmpty(password)) {
            password = user.getPassword();
        } else {
            password = CommonUtils.sha1(Lists.newArrayList(password));
        }
        userMapper.updateUser(uid, username, password, status);
        return true;
    }

    @Override
    public boolean deleteUser(Integer uid) {
        if (userMapper.findUserByUid(uid) == null) {
            throw new MainException(ResultEnum.ERR_CAN_NOT_FIND_USER);
        }
        userMapper.deleteUser(uid);
        return true;
    }

    @Override
    public boolean resetPassword(Integer uid, String password, String oldPassword) {
        com.exemple.demo.entity.User user = userMapper.findUserByUid(uid);
        if (user == null) {
            throw new MainException(ResultEnum.ERR_CAN_NOT_FIND_USER);
        }
        if (Strings.isNullOrEmpty(oldPassword)
                || !CommonUtils.sha1(Lists.newArrayList(oldPassword)).equals(user.getPassword())) {
            throw new MainException(ResultEnum.ERR_WRONG_OLD_PWD);
        }
        userMapper.resetPassword(uid, CommonUtils.sha1(Lists.newArrayList(password)));
        return true;
    }

    @Override
    public boolean setRoles(Integer uid, List<Integer> roleIds) {
        if (userMapper.findUserByUid(uid) == null) {
            throw new MainException(ResultEnum.ERR_CAN_NOT_FIND_USER);
        }
        userMapper.removeAllRoles(uid);
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.setRoles(uid, roleIds);
        }
        return true;
    }

    @Override
    public User userInfo(Integer uid) {
        if (uid == null) {
            return null;
        }
        return this.transform(userMapper.findUserByUid(uid), null);
    }
}

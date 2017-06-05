package com.exemple.demo.sevice;

import com.google.common.collect.Lists;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.Exception.MainException;
import com.exemple.demo.entity.User;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.mapper.UserMapper;
import com.exemple.demo.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class LoginService implements ILoginService {
    private final static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer login(String username, String password) {
        User user = userMapper.findUserByUsername(username);
        if (user != null && user.getStatus() != SqlParam.StatusEnabled) {
            throw new MainException(ResultEnum.ERR_USER_DISABLED);
        }
        return user != null && CommonUtils.sha1(Lists.newArrayList(password)).equals(user.getPassword())
                ? user.getId()
                : -1;
    }
}
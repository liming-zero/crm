package com.bjpowernode.crm.service;

import com.bjpowernode.crm.domain.User;
import com.bjpowernode.crm.exception.LoginException;

import java.util.List;


public interface UserService {
    User queryUser(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}

package org.brower.service;

import org.brower.pojo.RegisterUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserLoginRegisterServiceImplTest {

    @Autowired
    UserLoginRegisterService userLoginRegisterService;

    @Autowired
    RegisterUser user;

    @Test
    void addUserToMysql() {
        user.setUserEmail("11.com");
        user.setUserPassword("111");
        user.setUserName("111");
        userLoginRegisterService.addUserToMysql(user);
    }
}
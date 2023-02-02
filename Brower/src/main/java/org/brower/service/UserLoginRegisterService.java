package org.brower.service;

import org.brower.pojo.RegisterUser;

public interface UserLoginRegisterService {

    String getRegisterCode(String username);

    void sendRegisterCodeByEmail(String code, String email);

    byte confirmCode(String code, String email);

    byte checkPassword(String password, String passwordAgain);

    byte addUserToMysql(RegisterUser user);

    boolean confirmLogin(String email, String password);

}

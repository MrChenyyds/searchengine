package org.brower.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;

import org.brower.pojo.ComfirmEmailResult;
import org.brower.pojo.PasswordCheckResult;
import org.brower.pojo.RegisterUser;
import org.brower.pojo.SimpleUser;
import org.brower.service.UserLoginRegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginRegisterPageControl {

    @Autowired
    private UserLoginRegisterService userLoginRegisterService;

    @GetMapping(value = "/home")
    public String getHomePage(HttpServletRequest request) {
        return "login";
    }

    @GetMapping (value = "/home_login_success")
    public String startSearch(HttpServletRequest request) {
        return "home";
    }

    @GetMapping(value = "/")
    public String firstView(HttpServletRequest request) {
        return "login";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(HttpServletRequest request) {
        return "login";
    }

    @GetMapping(value = "/register")
    public String getRegisterPage(HttpServletRequest request) { return "register"; }

    @PostMapping(value = "/getcode")
    public void getRegisterCode(HttpServletRequest request, @RequestParam("userEmail") String userEmail) {
        String code = userLoginRegisterService.getRegisterCode(userEmail);
        userLoginRegisterService.sendRegisterCodeByEmail(code, userEmail);
        return;
    }

    @PostMapping(value = "/registerConfirm")
    @ResponseBody
    public Map<String,String> confirmCode(@RequestBody RegisterUser user) {
//        JSON.parse( request.getParameter("user"));
        byte confirmCodeResult = userLoginRegisterService.confirmCode(user.getCode(), user.getUserEmail());
        byte addToMysqlresult= userLoginRegisterService.addUserToMysql(user);
        byte passwordResult =  userLoginRegisterService.checkPassword(user.getUserPassword(), user.getUserPasswordAgain());


//        byte confirmCodeResult =1;
//        byte addToMysqlresult= 1;
//        byte passwordResult =2;
        String outMessage="";
        switch (confirmCodeResult) {
            case ComfirmEmailResult.DIFFERENT:
                outMessage+="your code is incorrect!!!!\n";
                break;
            case ComfirmEmailResult.TIMEOUT:
                outMessage+="time out,please try again!!!\n";
                break;
        }
        switch (addToMysqlresult) {
            case ComfirmEmailResult.TIMEOUT:
                outMessage+="database error,register fail!!!!\n";
                break;
            case ComfirmEmailResult.DIFFERENT:
                outMessage+="email has been used!!!!\n";
                break;

        }
        switch (passwordResult) {
            case PasswordCheckResult.PASSWORD_DIFF:
                outMessage+="your password is different!!!\n";
                break;
            case PasswordCheckResult.PASSWORD_WEAK:
                outMessage+="your password is weak!!!\n";
                break;
            case PasswordCheckResult.PASSWORD_SHORT:
                outMessage+="your password is short!!!\n";
                break;
            case PasswordCheckResult.PASSWORD_WRONG:
                outMessage+="your password contain 空格或汉字!!!\n";
                break;
        }
        Map<String,String> m=new HashMap<String,String>();
        m.put("res",outMessage);

        return m;
    }

    @PostMapping(value = "/loginconfirm")
    @ResponseBody
    public Map<String, String> loginConfirm(@RequestBody SimpleUser user) {

        boolean confirmResult = userLoginRegisterService.confirmLogin(user.getUserEmail(), user.getUserPassword());

        String result=null;
        if (!confirmResult) {
            result="your password is incorrect!!!!";
        }
        Map<String,String> m=new HashMap<String,String>();
        m.put("result",result);
        return m;
    }

}
package org.brower.service;

import jakarta.annotation.Resource;

import org.brower.pojo.ComfirmEmailResult;
import org.brower.pojo.Email;
import org.brower.pojo.PasswordCheckResult;
import org.brower.pojo.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class UserLoginRegisterServiceImpl implements UserLoginRegisterService{

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    EmailService  emailService;

    @Override
    public String getRegisterCode(String username) {

        String key = username+"RegisterCode";
        Random random=new Random();
        int code = random.nextInt(900000)+100000;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key,String.valueOf(code),5, TimeUnit.MINUTES);
        return String.valueOf(code);
    }

    @Override
    public void sendRegisterCodeByEmail(String code, String emailAddress) {
        Email email = new Email();
        email.setUser(new String[]{emailAddress});
        email.setSubject("201search注册验证码");
        String content=String.format("hello %s:\n" +
                ".this is your register code:\n" +
                " ----------------------------------------------------------------\n" +
                "%s\n" +
                " ----------------------------------------------------------------\n" +
                "Please enter your code in 5 minutes\n" ,emailAddress,code);
        email.setContent(content);
        emailService.sendEmail(email);
    }

    @Override
    public byte confirmCode(String code, String email) {
        String key = email+"RegisterCode";
        if(redisTemplate.hasKey(key)) {
            String s = (String) redisTemplate.opsForValue().get(key);
            if(s.equals(code)) {
                return ComfirmEmailResult.SUCCESS;
            }
            return ComfirmEmailResult.DIFFERENT;
        }
        return ComfirmEmailResult.TIMEOUT;
    }

    @Override
    public byte checkPassword(String password, String passwordAgain) {
         if(!password.equals(passwordAgain)){
             return PasswordCheckResult.PASSWORD_DIFF;
         }
         if(judgePasswordWeak(password)){
             return PasswordCheckResult.PASSWORD_WEAK;
         }
         if(judgePasswordWrong(password)){
             return PasswordCheckResult.PASSWORD_WRONG;
         }
         if(password.length()<6){
             return PasswordCheckResult.PASSWORD_SHORT;
         }
         return PasswordCheckResult.PASSWORD_OK;
    }

    private boolean judgePasswordWrong(String password) {
        String s="[\\u4e00-\\u9fa5 ]";
        return Pattern.compile(s).matcher(password).find();
    }

    private boolean judgePasswordWeak(String password) {
//        ~!@#\$%\^\&\*\(\)_\+`-\={}\|:"<>\?\[\]\\;',\./
        String[] s={"[0-9]","[A-Z]","[a-z]","[^0-9a-zA-Z]"};
        return Arrays.stream(s).anyMatch(i->!Pattern.compile(i).matcher(password).find());
    }

    @Override
    public byte addUserToMysql(RegisterUser user) {
        String sql1="select count(*) from user where userEmail= ?";
        Integer query = jdbcTemplate.queryForObject(sql1, Integer.class, user.getUserEmail());
        if(query==0){
            String sql="INSERT INTO user value(null,?,?,?)";
            Object[] args={user.getUserName(),user.getUserEmail(),user.getUserPassword()};
            int update = jdbcTemplate.update(sql, args);
            return update==1?ComfirmEmailResult.SUCCESS:ComfirmEmailResult.TIMEOUT;
        }
        return ComfirmEmailResult.DIFFERENT;
    }

    @Override
    public boolean confirmLogin(String email, String password) {
        String sql="select userPassword from user where userEmail =? or userName =? ";
        Object[] args = {email,email};
        String passwordFromSql = jdbcTemplate.queryForObject(sql, String.class,args);
        return password.equals(passwordFromSql);
    }

}

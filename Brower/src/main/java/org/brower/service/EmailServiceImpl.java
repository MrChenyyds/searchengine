package org.brower.service;

import jakarta.annotation.Resource;

import org.brower.pojo.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderName;

    @Override
    public void sendEmail(Email email) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(senderName);
        // 邮件接收人
        mailMessage.setTo(email.getUser());
        // 邮件主题
        mailMessage.setSubject(email.getSubject());
        // 邮件内容
        mailMessage.setText(email.getContent());

        mailSender.send(mailMessage);
    }
}

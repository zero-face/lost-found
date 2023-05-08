package com.example.lostfound.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/5/14 0:13
 * @Since 1.8
 **/

/**
 * 需要在配置文件中配置用户名、密码等信息
 */
@Component
public class MailUtils {

    @Resource
    private JavaMailSenderImpl javaMailSender;

    public Boolean sendMailCode(String code,String receiver) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人邮件地址(上面获取到的，也可以直接填写,string类型)
        message.setFrom("clzeroface@163.com");
        //要发送的qq邮箱(收件人地址)
        message.setTo(receiver);//address
        //邮件主题
        message.setSubject("Lost&Found实名认证");
        //邮件正文
        message.setText(new Date()+"\n" +"您好！欢迎使用Lost&Found,您的验证码是" + code + "，本验证码在5分钟内有效，为了您的账号安全，请勿泄露此验证码！！！");//！！！
        javaMailSender.send(message);
        return true;
    }
    /**
     * @description 参数maps中需包含subject、fromBirth、toBirth;可选text;若包含attachment，则file为空
     * @param maps
     * @param file
     * @return
     * @throws MessagingException
     */
    public Boolean sendMail(String sender,String receiver,Map<String,String> maps,MultipartFile file) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
        helper.setFrom(sender);
        helper.setTo(receiver);
        getParam(helper,maps,file);
        javaMailSender.send(mimeMessage);
        return true;
    }

    private void getParam(MimeMessageHelper helper, Map<String, String> maps, MultipartFile file) throws MessagingException, IOException {
        helper.setSubject(maps.get("subject"));
        if(null !=maps.get("text")){
            helper.setText(maps.get("text"), true );
        }
        if(null != maps.get("attachment") && null != maps.get("fileBirth")){
            helper.addAttachment(maps.get("attachment"), new File(maps.get("fileBirth")));
        } else {
            helper.addAttachment(file.getOriginalFilename(), file);
        }
    }
}

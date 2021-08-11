package com.example.lostfound.controller;


import com.example.lostfound.constant.RedisCode;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.utils.*;
import com.example.lostfound.validate.code.ImageCode;
import com.example.lostfound.validate.smscode.SmsCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@Validated
public class TUserController extends BaseController{

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MailUtils mailUtils;

    @GetMapping("mcode")
    public CommonReturnType mail(@RequestParam("mail") @NotBlank String mail) throws MessagingException, BusinessException {
        if(!CheckEmailAndTelphoneUtil.checkEmail(mail)) {
            throw new BusinessException(EmBusinessError.EMAIL_INVALID_FORMAT);
        }
        SmsCode smsCode = RandomSmsUtil.createSMSCode();
        redisTemplate.opsForValue().set(RedisCode.MAIL_CODE.getMsg() + mail, smsCode, 300 , TimeUnit.SECONDS);
        final Boolean isSend = mailUtils.sendMailCode(smsCode.getCode(), mail);
        if(!isSend) {
            log.info("邮件发送失败！");
           throw new BusinessException(EmBusinessError.EMAIL_SEND_FAILURE);
        }
        System.out.println("您的验证码信息为：" + smsCode.getCode() + "有效时间为：" + smsCode.getExpireTime());
        return CommonReturnType.success(null,"发送成功");
    }

    @GetMapping("/image")
    public void getImageCode(@RequestParam("username") String username, HttpServletResponse response) throws IOException {
        final ImageCode imageCode = ImageCodeUtil.createImageCode();
        redisTemplate.opsForValue().set(RedisCode.IMAGE_CODE.getMsg() + username, imageCode, 120 , TimeUnit.SECONDS);
        response.setContentType("image/jpeg;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
    }
}


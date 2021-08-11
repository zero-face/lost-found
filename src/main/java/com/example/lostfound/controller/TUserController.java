package com.example.lostfound.controller;


import com.example.lostfound.constant.RedisCode;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.utils.*;
import com.example.lostfound.validate.code.ImageCode;
import com.example.lostfound.validate.code.ImageDTO;
import com.example.lostfound.validate.smscode.SmsCode;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
@Api(tags = "用户登录注册接口")
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@Validated
public class TUserController extends BaseController{

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MailUtils mailUtils;

    @ApiOperation("邮箱登录获取验证码")
    @ApiOperationSupport(author = "zero")
    @ApiImplicitParam(name = "mail", value = "邮箱" , required = true, paramType = "query", dataType = "String")
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
    @ApiOperation("图形验证码获取")
    @ApiOperationSupport(author = "zero")
    @ApiImplicitParam(name = "username", value = "账户名称" , required = true, paramType = "query", dataType = "String")
    @GetMapping("/image")
    public void getImageCode(@RequestParam("username") String username, HttpServletResponse response) throws IOException {
        final ImageCode imageCode = ImageCodeUtil.createImageCode();
        final ImageDTO imageDTO = new ImageDTO(imageCode.getCode(), imageCode.getExpireTime());
        log.info(imageDTO.toString());
        redisTemplate.opsForValue().set(RedisCode.IMAGE_CODE.getMsg() + username, imageDTO, 120 , TimeUnit.SECONDS);
        response.setContentType("image/jpeg;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpStatus.OK.value());
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
    }
    //注册、增删改查
    @ApiOperation("账户密码登录测试接口")
    @ApiOperationSupport(author = "zero")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="username",value = "账户名称" , required = true, paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name ="password",value = "密码" , required = true, paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name ="imageCode",value = "图形验证码" , required = true, paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name ="timeStamp",value = "时间戳" , required = true, paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name ="sign",value = "签名" , required = true, paramType = "query", dataType = "String" )
    })
    @PostMapping("/login")
    public String testUserLogin() {
        return "success";
    }

    @ApiOperation("邮箱登录测试接口")
    @ApiOperationSupport(author = "zero")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="mail",value = "邮箱" , required = true, paramType = "query", dataType = "String" ),
            @ApiImplicitParam(name ="mailCode",value = "验证码" , required = true, paramType = "query", dataType = "String" )
    })
    @PostMapping("/mail")
    public String testMailLogin() {
        return "success";
    }
}


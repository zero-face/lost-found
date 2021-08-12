package com.example.lostfound.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.constant.RedisCode;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.UserVO;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.*;
import com.example.lostfound.validate.code.ImageCode;
import com.example.lostfound.validate.code.ImageDTO;
import com.example.lostfound.validate.smscode.SmsCode;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private TUserService userService;

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

    /**
     * 用户注册
     * @param username
     * @param password
     * @param imageCode
     * @return
     */
    @PostMapping
    public CommonReturnType registery(@RequestParam("username")@NotBlank String username,
                                      @RequestParam("password")@NotBlank String password,
                                      @RequestParam("imageCode")@NotBlank String imageCode) {
        boolean aBoolean = userService.checkUsername(username);
        if(!aBoolean) {
            return CommonReturnType.fail("用户名已存在！","注册存在" );
        }
        final TUser user = new TUser(){{
            setNickName(username);
            setPassword(password);
        }};
        boolean save = userService.save(user);
        if(save) {
            log.info("注册成功");
            return CommonReturnType.success(null,"注册成功");
        }
        return CommonReturnType.fail(null,"注册失败");
    }

    /**
     * 更改用户信息
     * @param user
     * @param oldUsername
     * @return
     * @throws BusinessException
     */
    @PutMapping
    public CommonReturnType modifyUserInfo(@RequestBody TUser user,
                                           @RequestParam("oldUsername")@NotBlank String oldUsername) throws BusinessException {
        final boolean update = userService.modifyUserInfo(user,oldUsername);
        if(update) {
            log.info("修改成功");
            return CommonReturnType.success(null,"修改成功");
        } else {
            log.info("修改失败");
            return CommonReturnType.fail(null,"修改失败");
        }
    }

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    @GetMapping
    public CommonReturnType getUserInfo(@RequestParam("username")@NotBlank String username) {
        final TUser nick_name = userService.getOne(new QueryWrapper<TUser>().eq("nick_name", username));
        if(nick_name != null) {
            UserVO uerVO = new UserVO();
            BeanUtils.copyProperties(nick_name, uerVO);
            return  CommonReturnType.success(uerVO,"获取成功");
        }
        return CommonReturnType.fail(null,"获取失败");
    }


}


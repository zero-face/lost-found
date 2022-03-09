package com.example.lostfound.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.lostfound.dao.TUserMapper;
import com.example.lostfound.enums.RedisCode;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.vo.UserVO;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.*;
import com.example.lostfound.validate.code.ImageCode;
import com.example.lostfound.validate.code.ImageDTO;
import com.example.lostfound.validate.smscode.SmsCode;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private TUserService userService;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;
    private String OPENID_URL= "https://api.weixin.qq.com/sns/jscode2session";
//
//    @Autowired
//    private TUserMapper userMapper;

//    @GetMapping("/test")
//    public String test(@RequestParam("id") Integer id, @RequestParam("name")String name) {
//        TUser test = null;
//        if(id != null) {
//            if(name != null) {
//                test = userMapper.test(id, name);
//            } else {
//                test = userMapper.test(id);
//            }
//        } else {
//            userMapper.test();
//        }
//
//        System.out.println(test);
//        return test.toString();
//    }


    @ApiOperation("邮箱登录获取验证码")
    @ApiOperationSupport(author = "zero")
    @ApiImplicitParam(name = "mail", value = "邮箱" , required = true, paramType = "query", dataType = "String")
    @GetMapping("/mcode")
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
        return CommonReturnType.success(smsCode.getCode(),"发送成功");
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
            return CommonReturnType.fail("用户名已存在！","注册失败" );
        }
        final Boolean aBoolean1 = userService.checkImageCode(imageCode, username);
        if(null == aBoolean1 || !aBoolean1) {
            return CommonReturnType.fail("验证码错误","验证失败");
        }
        final TUser user = new TUser(){{
            setNickName(username);
            setAddressUrl("https://tse4-mm.cn.bing.net/th/id/OIP-C.l5oteGIsGxT9JvLDC2rC2gHaKL?w=200&h=275&c=7&o=5&dpr=1.12&pid=1.7");
            setPassword(passwordEncoder.encode(password));
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
     * @param
     * @param
     * @return
     * @throws BusinessException
     */
    @PutMapping
    public CommonReturnType modifyUserInfo(@RequestParam(value = "name",required = false)String name,
                                           @RequestParam(value = "pic",required = false)String pic,
                                           @RequestParam(value = "sex",required = false)String sex,
                                           @RequestParam(value = "qq",required = false)String qq,
                                           @RequestParam("id")@NotNull Integer id) throws BusinessException {
        if(name != null) {
            final TUser nickName = userService.getOne(new QueryWrapper<TUser>().eq("nick_name", name));
            if(nickName !=null) {
                return CommonReturnType.fail("昵称已被占用", "修改失败");
            }
        }
        final TUser id1 = userService.getOne(new QueryWrapper<TUser>().eq("id", id));

        if(id1 == null) {
            return CommonReturnType.fail("没有该用户","修改失败");
        }
        id1.setNickName(name);
        id1.setAddressUrl(pic);
        id1.setSex(sex);
        id1.setQq(qq);
        final boolean update = userService.update(id1,new UpdateWrapper<TUser>().eq("id", id));
        if(update) {
            log.info("修改成功");
            return CommonReturnType.success(null,"修改成功");
        } else {
            log.info("修改失败");
            return CommonReturnType.fail(null,"修改失败");
        }
    }

    /**
     * 实名认证
     * @param
     * @return
     */
    @PostMapping("/auth")
    public CommonReturnType realNameAuthentication(@RequestParam("name") String trueName,
                                                   @RequestParam("number") String number,
                                                   @RequestParam("tel") String tel,
                                                   @RequestParam("collage") String collage,
                                                   @RequestParam("clazz") String clazz,
                                                   @RequestParam("uid") Integer id) {
        final TUser id1 = userService.getOne(new QueryWrapper<TUser>().eq("id", id));
        if(null == id1) {
            return CommonReturnType.fail("没有该用户","获取失败");
        }
        id1.setTrueName(trueName);
        id1.setNumber(number);
        id1.setTel(tel);
        id1.setCollage(collage);
        id1.setClazz(clazz);
        id1.setIsTrue(true);
        userService.update(id1, new UpdateWrapper<TUser>().eq("id", id));
        return CommonReturnType.success(null,"认证成功");
    }
    /**
     * 根据名字拿到信息（多余）
     * @param username
     * @return
     */
    @GetMapping("/info")
    public CommonReturnType getUserInfo(@RequestParam("username")String username) {
        final TUser nick_name = userService.getOne(new QueryWrapper<TUser>().eq("nick_name", username));
        if(null == nick_name) {
            return CommonReturnType.fail(null, "获取失败");
        }
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(nick_name, userVO);
        return CommonReturnType.success(userVO,"获取成功");
    }

    /**
     * 检测令牌是否有效
     * @param token
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @GetMapping("/check")
    public CommonReturnType checkJWT(@RequestParam("token")String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(null == token) {
            return CommonReturnType.fail(null,"检验失败");
        }
        final Boolean aBoolean = jwtTokenUtil.checkIsExpired(token);
        if(aBoolean) {
            return CommonReturnType.fail("令牌过期","检验失败");
        }
        return CommonReturnType.success("令牌未过期","检验成功");
    }

    /**
     * 微信登录
     * @param code
     * @param user
     * @return
     * @throws IllegalAccessException
     * @throws BusinessException
     * @throws InvocationTargetException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @PostMapping("/code")
    @ApiOperation("根据code获取openid并得到登录token")
    public CommonReturnType receiveCode(@RequestParam("code") String code,TUser user) throws BusinessException,
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("获取code开始=========》{}",code);
        //接收到临时登录的code，向微信服务器发起请求，获取openid,session_key，unionid
        Map<String,String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("code", code);
        map.put("secret", secret);
        String openid = userService.getOpenIdByCode(OPENID_URL, map);
        log.info("获取到openid:{}=========》",openid);
        //查询数据库中是否含有这个openID，如果有，说明已经授权，查询用户信息，返回信息以及token；没有则保存，但是其他的用户信息是空，返回用户信息以及token
        final TUser one = userService.getOne(new QueryWrapper<TUser>().eq("open_id", openid));
        if(null == one) {
            log.info("不存在存在该用户，正在登陆....");
            final TUser tUser = new TUser();
            BeanUtils.copyProperties(user, tUser);
            tUser.setOpenId(openid);
            userService.save(tUser);
        }else {
            final TUser tUser = new TUser();
            System.out.println("传输的用户信息：" + user);
            BeanUtils.copyProperties(one, tUser);
            tUser.setOpenId(openid);
            System.out.println("userinfo"+tUser);
            userService.update(tUser,new UpdateWrapper<TUser>().eq("open_id", openid));
        }
        final Map<String, Object> maps = new HashMap<String, Object>(){{
            put("username", user.getNickName());
        }};
        final String token = jwtTokenUtil.generateToken(maps, user.getNickName(), 24 * 60 * 60 * 1000);
        final TUser id = userService.getOne(new QueryWrapper<TUser>().eq("open_id", openid));
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(id, userVO);
        Map<String, Object> reToken = JSON.parseObject(JSON.toJSONString(userVO), new TypeReference<Map<String,
                Object>>() {
        });
        reToken.put("token",token);
        return CommonReturnType.success(reToken);
    }

    /**
     * 获取用户信息（根据user或者userid 都可以）
     * @param username
     * @return
     */
    @GetMapping
    public CommonReturnType getUserInfo(@RequestParam(value = "username",required = false) String username,
                                        @RequestParam(value = "id",required = false) Integer id) {
        if (StringUtils.isEmpty(username) && null == id) {
            return CommonReturnType.fail(null, "参数错误");
        }

        final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(username, id);
        if (userInfoByNameOrId != null) {
            UserVO uerVO = new UserVO();
            BeanUtils.copyProperties(userInfoByNameOrId, uerVO);
            return CommonReturnType.success(uerVO, "获取成功");
        }
            return CommonReturnType.fail(null, "获取失败");
    }
}




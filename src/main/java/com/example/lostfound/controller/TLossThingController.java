package com.example.lostfound.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.*;
import com.example.lostfound.entity.vo.AuditVO;
import com.example.lostfound.entity.vo.LossDetailVO;
import com.example.lostfound.entity.vo.LossThingVO;
import com.example.lostfound.service.TAdminAuditService;
import com.example.lostfound.service.TLossCommontService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.NotifyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 * 实现的功能主要是首页对于失物的展示(采用分页查询)，失物的分类展示，失物的查询；失物的详细信息查询
 */
@RestController
@RequestMapping("/api/v1/lost")
@Api(tags = "失物模块接口")
@Slf4j
public class TLossThingController extends BaseController{

    @Autowired
    private TLossThingService lossThingService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TAdminAuditService adminAuditService;
    @Autowired
    private NotifyUtil notifyUtil;
    @Autowired
    private TUserService userService;
    @Autowired
    private TLossCommontService lossCommontService;

    /**
     * 拿出重要数据轮播展示
     * @return
     */
    @GetMapping("/all")
    public CommonReturnType getAll() {
        final ArrayList<Integer> integers = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(7);
        }};
        final List<TLossThing> list = lossThingService.list(new QueryWrapper<TLossThing>()
                .eq(true, "status", 0).orderByDesc("gmt_create")
                .isNotNull("picture_url").in("type",integers).last("limit 5"));
        if(null == list || list.size() < 1) {
            return CommonReturnType.fail(null,"获取失败");
        }
        final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(list);
        return CommonReturnType.success(lossThingVOS,"获取成功");
    }

    /**
     * 首页的分页查询
     * @param pn
     * @return
     */
    @GetMapping("/{pn}")
    @CachePut(value = "redisCache",key = "'RedisLose'+ #pn")
    public CommonReturnType getAllLost(@PathVariable("pn")Integer pn) {
        //查询第pn，每页5条，不查询数据总数
        final Page<TLossThing> page = new Page<>(pn, 5,false);
        final Page<TLossThing> lossThingPage = lossThingService.page(page,new QueryWrapper<TLossThing>()
                .eq("status", 0).orderByDesc("gmt_create"));
        final List<TLossThing> records = lossThingPage.getRecords();
        if(records != null || records.size() > 0) {
            final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(records);
            return CommonReturnType.success(lossThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");

    }

    /**
     * 获取所有数据
     * @return
     */
    @GetMapping("/list")
    public CommonReturnType getList() {
        //查询第pn，每页5条，不查询数据总数
        final List<TLossThing> lossThingPage = lossThingService.list(new QueryWrapper<TLossThing>()
                .eq("status", 0).orderByDesc("gmt_create"));
        if(lossThingPage != null || lossThingPage.size() > 0) {
            final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(lossThingPage);
            return CommonReturnType.success(lossThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");

    }

    /**
     * 失物分类型展示
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    @CachePut(value = "redisCache",key = "'RedisLose'+ #type",condition = "#result!=null")
    public CommonReturnType getLostByType(@PathVariable("type")Integer type) {
        final List<TLossThing> list = lossThingService.list(new QueryWrapper<TLossThing>().eq("type", type)
                .eq("status", 0).orderByDesc("gmt_create"));
        if(list == null || list.size() == 0) {
            return CommonReturnType.fail(null,"没有该类型的失物");
        }
        final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(list);
        return CommonReturnType.success(lossThingVOS,"查找成功");
    }

    /**
     * 搜索
     * @param search 搜索的关键字
     * @param time 时间
     * @return 失物的视图模型列表
     */
    @GetMapping
    public CommonReturnType getByFeature(@RequestParam(value = "s",required = true)String search,
                                         @RequestParam(value = "time",required = false)Integer time) {
        if(search==null ||search.trim() =="") {
            return CommonReturnType.fail("查询条件为空", "查询失败");
        }
        final List<TLossThing> lossBySearchAndTime = lossThingService.getLossBySearchAndTime(search, time);
        if (lossBySearchAndTime != null && lossBySearchAndTime.size() > 0) {
            final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(lossBySearchAndTime);
            return CommonReturnType.success(lossThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");
    }

    /**
     * 获取失物详细信息（失物信息，评论区,需要两次分开请求）
     * @param id 失物id
     * @return 失物详细信息的视图模型
     */
    @GetMapping("/detail/{id}")
    public CommonReturnType getDetail(@PathVariable("id")Integer id) {
        final TLossThing byId = lossThingService.getOne(new QueryWrapper<TLossThing>()
                .eq(true, "status", 0).eq("id",id));
        if(byId !=null) {
            final LossDetailVO lossDetailVO = lossThingService.converTOLossDetailVO(byId);
            return CommonReturnType.success(lossDetailVO,"获取成功");
        }
        return CommonReturnType.fail(null, "获取失败");
    }

    @RequestMapping ("/upload")
    public CommonReturnType uploadFile(@NotNull MultipartFile picture, HttpServletRequest request) {
        log.info("收到上传的图片");
        final String picUrl = lossThingService.uploadImage(picture);
        if(StringUtils.isEmpty(picUrl)) {
            log.info("图片上传失败");
            return CommonReturnType.fail(null, "图片上传失败");
        }
        return CommonReturnType.success(picUrl,"上传成功");
    }

    /**
     * 失物发布
     * @param name
     * @param picture
     * @param address
     * @param type
     * @param time
     * @param userId
     * @param description
     * @return
     */
    @JsonFormat
    @RequestMapping ("/publoss")
    public CommonReturnType publishLoss(@RequestParam(value = "name")String name,
                                        @RequestParam(value = "pic",required = false)String picture,
                                        @RequestParam(value = "address")String address,
                                        @RequestParam(value = "type")Integer type,
                                        @RequestParam(value = "lossTime",required = false) Date time,
                                        @RequestParam(value = "userId")Integer userId,
                                        @RequestParam(value = "des")String description
                                        ) {
//        //保存图片
//        final String picUrl = lossThingService.uploadImage(picture);
//        if(StringUtils.isEmpty(picUrl)) {
//            log.info("图片上传失败");
//            return CommonReturnType.fail(null, "图片上传失败");
//        }/**/
        final TLossThing tLossThing = new TLossThing() {{
            setName(name);
            setAddress(address);
            setDescription(description);
            setLossUserId(userId);
            setGmtCreate(time);
            setType(type);
            setPictureUrl(picture);
        }};
        final boolean save = lossThingService.save(tLossThing);
        if(save) {
            redisTemplate.opsForValue().set("LOSS_COMMENT_" + tLossThing.getId(), 0);
            return CommonReturnType.success(null,"发布成功");
        }
        return CommonReturnType.fail(null,"发布失败");
    }

    /**
     * 查询一个用户发布的失物
     * @param id
     * @return
     */
    @GetMapping("/mpub/{id}")
    public CommonReturnType getLossById(@PathVariable("id")Integer id) {
        final List<TLossThing> list = lossThingService.list(new QueryWrapper<TLossThing>()
                .eq(true, "loss_user_id", id).orderByDesc("gmt_create"));
        if(list == null) {
            return CommonReturnType.fail(null,"获取失败");
        }
        final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(list);
        return CommonReturnType.success(lossThingVOS,"获取成功");
    }
    /**
     * 申请认领
     * @param lossId
     * @param userId
     * @return
     */
    @GetMapping("/claim")
    public CommonReturnType claim(@RequestParam("lossId")Integer lossId,
                                  @RequestParam("userId")Integer userId) {
        //构建一条审核信息
        final TAdminAudit tAdminAudit = new TAdminAudit() {{
            setLossId(lossId);
            setUserId(userId);
        }};
        //存入数据库
        adminAuditService.save(tAdminAudit);
        //实时通知后台，goeasy
        final AuditVO auditVO = lossThingService.packageNotifyMes(tAdminAudit.getId(),userId, lossId);
        final boolean publish = notifyUtil.publish(JSON.toJSONString(auditVO),"user_notify_admin");
        if(publish) {
            return CommonReturnType.success(tAdminAudit,"申请成功");
        }
        return CommonReturnType.fail(null,"申请失败");
    }
}


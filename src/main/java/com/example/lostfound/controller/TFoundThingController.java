package com.example.lostfound.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.*;
import com.example.lostfound.entity.vo.LossThingVO;
import com.example.lostfound.entity.vo.TFoundThingVO;
import com.example.lostfound.service.TAdminAuditService;
import com.example.lostfound.service.TFoundThingService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.MesUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/api/v1/found")
@Api(tags = "寻物接口")
@Validated
@Slf4j
public class TFoundThingController extends BaseController{
    @Autowired
    private TFoundThingService foundThingService;
    @Autowired
    private TLossThingService lossThingService;

    @Autowired
    private TUserService userService;

    @Autowired
    private TAdminAuditService adminAuditService;


    /**
     * 发布寻物
     * @param name
     * @param pic
     * @param address
     * @param lossTime
     * @param des
     * @param type
     * @param userId
     * @return
     * @throws BusinessException
     */
    @GetMapping("/pubfound")
    public CommonReturnType publishFound(@RequestParam("name")@NotBlank String name,
                                         @RequestParam(value = "pic",required = false)String pic,
                                         @RequestParam("address")@NotBlank String address,
                                         @RequestParam("lossTime")@NotNull long lossTime,
                                         @RequestParam("des")@NotBlank String des,
                                         @RequestParam("type")@NotBlank String type,
                                         @RequestParam("userId") Integer userId,
                                         @RequestParam("area")String area) throws BusinessException {
        TFoundThing foundThing = new TFoundThing() {{
            setName(name);// 名称
            setAddress(address); //地址
            setDescription(des); //描述
            setLoseTime(lossTime); //丢失时间
            setPictureUrl(pic); //图片
            setPublishUserId(userId); //发布人
            setType(type); //发布类型
            setArea(area);
            setStatus(true);
        }};
        //放入寻物表
        final boolean save = foundThingService.save(foundThing);
        if(save) {
            // 插入管理员表
            final TAdminAudit admin = new TAdminAudit(){{
                setFoundId(foundThing.getId());
                setUserId(userId);
                setStatus("0");
            }};
            adminAuditService.save(admin);

            final List<TUser> adminUser = userService.list(new QueryWrapper<TUser>().eq("admin", "1"));
            for(int i = 0; i < adminUser.size(); i++) {
                // 通知管理员审核
                final TMessage pubLossMes = new TMessage();
                pubLossMes.setFroms(userId);
//            pubLossMes.setLossId(lossId);
                pubLossMes.setToo(adminUser.get(i).getId());
                pubLossMes.setMsgState("1"); // 默认已读
                pubLossMes.setTextType("0"); // 文字评论
                pubLossMes.setType("4"); // 发布通知

                final String sessionId = MesUtil.generateSessionId(String.valueOf(userId), "1");
                pubLossMes.setChatSessionId(sessionId);
                foundThingService.pubNotify(pubLossMes, foundThing);
            }
        }
        return CommonReturnType.success(null,"发布成功");
    }

    /**
     * 查找用户发布
     * @param id 用户id
     * @return 用户发布的信息的视图模型
     */
    @GetMapping("/mpub/{id}")
    public CommonReturnType getFoundById(@PathVariable("id")Integer id) {
        final List<TFoundThing> list = foundThingService.list(new QueryWrapper<TFoundThing>().eq("publish_user_id", id).orderByDesc("gmt_create"));
        if(list == null || list.size() < 1) {
            return CommonReturnType.fail(null,"获取失败");
        }
        final List<TFoundThingVO> tFoundThingVOS = foundThingService.converToLossVO(list);
        return CommonReturnType.success(tFoundThingVOS,"获取成功");
    }


    /**
     * 分页查询所有的寻物信息
     * @param pn
     * @return
     */
    @GetMapping("/founds/{pn}")
    public CommonReturnType getAllFoundThing(@PathVariable("pn")Integer pn,
                                            @NotNull @RequestParam("typeId")Integer typeId){
        //查询第pn，每页5条，不查询数据总数
        final Page<TFoundThing> page = new Page<>(pn, 5,false);
        final HashMap<String, Object> hashMap = new HashMap<String, Object>() {{
            put("status", 0);
            put("type", typeId);
        }};
        final Page<TFoundThing> lossThingPage = foundThingService.page(page,new QueryWrapper<TFoundThing>().eq(typeId == 0,"status", 0).allEq(typeId != 0, hashMap,false).orderByDesc("gmt_create"));
        final List<TFoundThing> records = lossThingPage.getRecords();
        if(records != null) {
            if(records.size() == 0) {
                return CommonReturnType.fail(null, "没有更多物品了");
            }
            final List<TFoundThingVO> tFoundThingVOS = foundThingService.converToLossVO(records);
            return CommonReturnType.success(tFoundThingVOS,"查询成功");
        }

        return CommonReturnType.fail(null, "查询失败");
    }

    /**
     * 搜索
     * @param search 搜索的关键字
     * @param time 时间
     * @return 失物的视图模型列表
     */
    @GetMapping
    public CommonReturnType getByFeature(@RequestParam(value = "s",required = true)String search,
                                         @RequestParam(value = "time",required = false)Integer time,
                                         @RequestParam(value = "type") Integer type) {
        if(search==null ||search.trim() =="") {
            return CommonReturnType.fail("查询条件为空", "查询失败");
        }
        final List<TFoundThing> lossBySearchAndTime = foundThingService.getLossBySearchAndTime(search, time, type);
        if (lossBySearchAndTime != null && lossBySearchAndTime.size() > 0) {
            final List<TFoundThingVO> tFoundThingVOS = foundThingService.converToLossVO(lossBySearchAndTime);
            return CommonReturnType.success(tFoundThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");
    }

    /**
     * 按类型查找寻物
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    public CommonReturnType getFoundBytype(@PathVariable("type")String type) {
        final List<TFoundThing> list = foundThingService.list(new QueryWrapper<TFoundThing>().eq("type", type).eq("status", 0));
        if(list == null || list.size() == 0) {
            return CommonReturnType.fail(null,"没有该类型的失物");
        }
        final List<TFoundThingVO> foundThingVOS = foundThingService.converToLossVO(list);
        return CommonReturnType.success(foundThingVOS,"查找成功");
    }

}


package com.example.lostfound.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TAdminAudit;
import com.example.lostfound.entity.TFoundLoss;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.*;
import com.example.lostfound.utils.NotifyUtil;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.entity.vo.MessageVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @Author Zero
 * @Date 2021/8/14 13:17
 * @Since 1.8
 * @Description
 **/
@RestController
@RequestMapping("/api/v1/admin")
@Api(tags = "管理员审核模块接口")
@Slf4j
public class TAdminAuditController extends BaseController{

    @Autowired
    private TAdminAuditService adminAuditService;
    @Autowired
    private NotifyUtil notifyUtil;
    @Autowired
    private TLossThingService lossThingService;
    @Autowired
    private TFoundLossService foundLossService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TUserService userService;

    /**
     * 管理员审核请求的结果
     * @param type
     * @param
     * @param des
     * @return
     */
    @GetMapping("/audit")
    public CommonReturnType audit(@RequestParam("type")String type, //审核结果是否同意
                                  @RequestParam("auditId")Integer mesId, //认领信息的id
                                  @RequestParam("des")String des, //描述
                                  @RequestParam("adminId")Integer adminId, //管理员id
                                  @RequestParam("applyId")Integer applyId) { //认领人id
        final TLossThing id = lossThingService.getById(mesId);
        final MessageVO messageVO = new MessageVO() {{
            setStatus(true);
            setId(UUID.randomUUID().toString());
            setFlag(false);
            setFroms(adminId);
            setToo(applyId);
            setType("0");
            setMessage(des);
        }};
        messageService.save(messageVO);
        //更改状态
        final TAdminAudit one = adminAuditService.getOne(new QueryWrapper<TAdminAudit>().eq("loss_id", mesId));
        one.setGmtModified(new Date());
        one.setStatus(true);
        one.setAdminId(adminId); //设置管理员
        adminAuditService.updateById(one);
        final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, applyId);
        final MesVO mesVO = new MesVO();
        mesVO.setName(id.getName());
        BeanUtils.copyProperties(userInfoByNameOrId, mesVO);
        BeanUtils.copyProperties(messageVO, mesVO);
        if(type .equals("1") ) { //同意
            id.setStatus(true);
            lossThingService.updateById(id);
            final TFoundLoss tFoundLoss = new TFoundLoss() {{
                setAuditId(mesId); //审核信息id
                setLossId(id.getId()); //失物id
                setUserId(applyId); //领取人id
            }};
            //放入找回表
            foundLossService.save(tFoundLoss);
        }
        //回传原因
        notifyUtil.publish(JSON.toJSONString(mesVO),applyId.toString(),mesId.toString());
        return CommonReturnType.success("操作成功");
    }



}

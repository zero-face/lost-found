package com.example.lostfound.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.*;
import com.example.lostfound.entity.vo.AuditVO;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import com.example.lostfound.service.*;
import com.example.lostfound.utils.MesUtil;
import com.example.lostfound.utils.NotifyUtil;
import com.example.lostfound.entity.vo.MesVO;
import com.example.lostfound.utils.WxUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private TLossThingService lossThingService;
    @Autowired
    private TFoundLossService foundLossService;
    @Autowired
    private TFoundThingService foundThingService;
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
    public CommonReturnType audit(@RequestParam("type")Integer type,
                                  @RequestParam("id")Integer id, //审核信息ID
                                  @RequestParam("des")String des, //描述
                                  @RequestParam("adminId")Integer adminId) {
        TAdminAudit audit = new TAdminAudit() {{
            setStatus(String.valueOf(type));
            setDes(des);
            setAdminId(adminId);
        }};
        adminAuditService.update(audit, new QueryWrapper<TAdminAudit>().eq("id", id));
        audit = adminAuditService.getById(id);

        String con ="";
        if(type == 1) {
            if(audit.getFoundId() != null) {
                final TFoundThing foundThing = foundThingService.getById(audit.getFoundId());
                con =  "#" + foundThing.getName() + "# 审核通过";
            }
            if (audit.getLossId() != null) {
                final TLossThing lossThing = lossThingService.getById(audit.getLossId());
                con = "#" + lossThing.getName() +  "# 审核通过";
            }

        } else {
            con = "审核驳回，原因：" + des;
        }

        final String sessionId = MesUtil.generateSessionId(String.valueOf(audit.getUserId()), String.valueOf(adminId));
        TMessage messageVO = new TMessage();
        messageVO.setMsgState("0");
        messageVO.setFroms(adminId);
        messageVO.setToo(audit.getUserId());
        if(type == 1) {
            messageVO.setLossId(audit.getLossId());
        }
        messageVO.setType("0");
        messageVO.setTextType("0");
        messageVO.setSendText(con);
        messageVO.setChatSessionId(sessionId);

        messageService.save(messageVO);
        boolean flag = false;
        Integer lossId = null;
        if(audit.getLossId() != null) {
            flag = true;
            lossId = audit.getLossId();
        } else {
            lossId = audit.getFoundId();
        }
        adminAuditService.checkNotify(lossId,messageVO, type, flag);

        if(type == 1) {
            if(audit.getLossId() != null) {
                final TLossThing lossThing = new TLossThing() {{
                    setStatus(false);
                }};
                lossThingService.update(lossThing, new QueryWrapper<TLossThing>().eq("id", audit.getLossId()));
            }
            if(audit.getFoundId() != null) {
                final TFoundThing foundThing = new TFoundThing() {{
                    setStatus(false);
                }};
                foundThingService.update(foundThing, new QueryWrapper<TFoundThing>().eq("id", audit.getFoundId()));
            }

        }
        final CommonReturnType checkList = getCheckList();
        return checkList;

    }

    @GetMapping
    public CommonReturnType getCheckList() {
        final List<TAdminAudit> list = adminAuditService.list(new QueryWrapper<TAdminAudit>().orderByAsc("admin_id").orderByDesc("gmt_create"));
        final List<AuditVO> collect = list.stream().map(auditVOitem -> {
            AuditVO auditVO = new AuditVO();
            BeanUtils.copyProperties(auditVOitem,auditVO);
            final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, auditVOitem.getUserId());
            TLossThing lossThing = null;
            TFoundThing foundThing = null;
            if(auditVOitem.getLossId() != null) {
                lossThing = lossThingService.getOne(new QueryWrapper<TLossThing>().eq("id", auditVOitem.getLossId()));
            }
           if(auditVOitem.getFoundId() != null) {
               foundThing = foundThingService.getOne(new QueryWrapper<TFoundThing>().eq("id", auditVOitem.getFoundId()));
           }
            auditVO.setNickName(userInfoByNameOrId.getNickName());
            auditVO.setUserId(auditVO.getUserId());
            auditVO.setAddressUrl(userInfoByNameOrId.getAddressUrl());
            auditVO.setApplyId(auditVOitem.getLossId());
            if(auditVOitem.getLossId() != null) {
                auditVO.setLossName(lossThing.getName());
                auditVO.setPicUrl(lossThing.getPictureUrl());
            }
            if(auditVOitem.getFoundId() != null) {
                auditVO.setLossName(foundThing.getName());
                auditVO.setPicUrl(foundThing.getPictureUrl());
            }
            auditVO.setGmtCreate(auditVOitem.getGmtCreate());
            auditVO.setGmtModified(auditVOitem.getGmtModified());
            auditVO.setStatus(auditVOitem.getStatus());
            return auditVO;
        }).collect(Collectors.toList());
        if(collect == null || collect.size() < 1) {
            return CommonReturnType.fail("暂无审核信息");
        }
        return CommonReturnType.success(collect);
    }

}

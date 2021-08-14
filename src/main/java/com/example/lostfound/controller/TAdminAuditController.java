package com.example.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TAdminAudit;
import com.example.lostfound.entity.TFoundLoss;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.service.TAdminAuditService;
import com.example.lostfound.service.TFoundLossService;
import com.example.lostfound.service.TLossCommontService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.utils.NotifyUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.smartcardio.CommandAPDU;

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

    /**
     * 管理员审核请求的结果
     * @param type
     * @param
     * @param des
     * @return
     */
    @GetMapping("/audit")
    public CommonReturnType audit(@RequestParam("type")String type,
                                  @RequestParam("auditId")Integer mesId,
                                  @RequestParam("des")String des,
                                  @RequestParam("adminId")Integer adminId,
                                  @RequestParam("applyId")Integer applyId) {
        if(type .equals("1") ) { //同意
            //更改状态
            final TAdminAudit one = adminAuditService.getOne(new QueryWrapper<TAdminAudit>().eq("id", mesId));
            one.setAuditTime(System.currentTimeMillis());
            one.setStatus(true);
            one.setAdminId(adminId); //设置管理员
            adminAuditService.updateById(one);
            //实时通知
            notifyUtil.publish("审核通过,请到尽快到规定地点认领失物","admin_notify_user");
            //更改失物状态
            final TLossThing byId = lossThingService.getById(one.getLossId());
            byId.setStatus(true);
            lossThingService.updateById(byId);
            final TFoundLoss tFoundLoss = new TFoundLoss() {{
                setAuditId(mesId); //审核信息id
                setLossId(byId.getId()); //失物id
                setUserId(applyId); //领取人id
            }};
            //放入找回表
            foundLossService.save(tFoundLoss);
            return CommonReturnType.success("操作成功");
        }
        //回传原因
        notifyUtil.publish(des,"admin_notify_user");
        return CommonReturnType.success("操作成功");
    }



}

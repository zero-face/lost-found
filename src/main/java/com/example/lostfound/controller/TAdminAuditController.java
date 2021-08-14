package com.example.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.TAdminAudit;
import com.example.lostfound.service.TAdminAuditService;
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
public class TAdminAuditController {

    @Autowired
    private TAdminAuditService adminAuditService;
    @Autowired
    private NotifyUtil notifyUtil;

    /**
     * 管理员审核请求的结果
     * @param type
     * @param auditId
     * @param des
     * @return
     */
    @GetMapping("/audit")
    public CommonReturnType audit(@RequestParam("type")String type,
                                  @RequestParam("auditId")Integer auditId,
                                  @RequestParam("des")String des) {
        if(type .equals("1") ) { //同意
            //更改状态
            final TAdminAudit one = adminAuditService.getOne(new QueryWrapper<TAdminAudit>().eq("id", auditId));
            one.setAuditTime(System.currentTimeMillis());
            one.setStatus(true);
            adminAuditService.updateById(one);
            //实时通知
            notifyUtil.publish("审核通过,请到尽快到规定地点认领失物","admin_notify_user");
            return CommonReturnType.success("操作成功");
        }
        //回传原因
        notifyUtil.publish(des,"admin_notify_user");
        return CommonReturnType.success("操作成功");
    }



}

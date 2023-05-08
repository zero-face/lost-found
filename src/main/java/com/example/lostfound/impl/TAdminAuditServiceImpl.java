package com.example.lostfound.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.dao.TAdminAuditMapper;
import com.example.lostfound.entity.TAdminAudit;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import com.example.lostfound.service.TAdminAuditService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author Zero
 * @Date 2021/8/14 13:14
 * @Since 1.8
 * @Description
 **/
@Service
public class TAdminAuditServiceImpl extends ServiceImpl<TAdminAuditMapper, TAdminAudit> implements TAdminAuditService {

    @Autowired
    private TLossThingService lossThingService;

    @Autowired
    private TUserService userService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private WxUtil wxUtil;

    @Override
    public void checkNotify(Integer lossId, TMessage messageVO, Integer type) {
        CompletableFuture.supplyAsync(() -> {
            try {
                template.convertAndSend("/exchange/sendToUser/" + messageVO.getToo(), JSON.toJSONString(messageVO));
                final TLossThing one = lossThingService.getOne(new QueryWrapper<TLossThing>().eq("id", lossId));
                // 微信通知
                final TUser user = userService.getUserInfoByNameOrId(null, messageVO.getToo());
                final String openId = user.getOpenId();
                final String accessToken = wxUtil.getAccessToken();
                String state = "审核通过";
                if(type == 2) {
                    state = "审核失败";
                }
                String finalState = state;

                final PubNotify pubNotify = new PubNotify() {{
                    setThing2(new WxEntity(messageVO.getSendText()));
                    setThing1(new WxEntity(one.getName()));
                    setTime3(new WxEntity(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(one.getGmtCreate())));
                    setPhrase4(new WxEntity(finalState));
                }};
                final Map<String, Object> notifyBody = wxUtil.builderPub(openId, pubNotify);
                wxUtil.postSubMes(accessToken, notifyBody);
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });

    }
}

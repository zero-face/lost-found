package com.example.lostfound.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.dao.TUserMapper;
import com.example.lostfound.entity.TFoundThing;
import com.example.lostfound.dao.TFoundThingMapper;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.vo.TFoundThingVO;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TFoundThingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.WxUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Service
public class TFoundThingServiceImpl extends ServiceImpl<TFoundThingMapper, TFoundThing> implements TFoundThingService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private TFoundThingMapper foundThingMapper;

    @Autowired
    private TUserService userService;

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private WxUtil wxUtil;
    @Autowired
    private MessageService messageService;

    @Override
    public List<TFoundThingVO> converToLossVO(List<TFoundThing> list) {
        final List<TFoundThingVO> thingVOList = list.stream().map(e -> {
            final TFoundThingVO tFoundThingVO = new TFoundThingVO();
            BeanUtils.copyProperties(e, tFoundThingVO);
            final TUser user = userMapper.selectOne(new QueryWrapper<TUser>().eq(e.getPublishUserId() != null, "id", tFoundThingVO.getPublishUserId()));
            tFoundThingVO.setNickName(user.getNickName());
            tFoundThingVO.setUserAddress(user.getAddressUrl());
            tFoundThingVO.setUserId(user.getId());
            return tFoundThingVO;
        }).collect(Collectors.toList());
        return thingVOList;
    }

    @Override
    public List<TFoundThing> getLossBySearchAndTime(String search, Integer time, Integer type) {
        final List<TFoundThing> tLossThings = foundThingMapper.getFoundBySearchAndTime(search, time, type);
        return tLossThings;
    }

    @Override
    public void pubNotify(TMessage pubLossMes, TFoundThing foundThing) {
        CompletableFuture.supplyAsync(() -> {
            try {
                final TUser user = userService.getOne(new QueryWrapper<TUser>().eq("id", pubLossMes.getToo()));
                String notifyContent = user.getNickName() + "发布了一条寻物启示信息";
                pubLossMes.setSendText(notifyContent);
                // 线上消息发送
                template.convertAndSend("/exchange/sendToUser/" + pubLossMes.getToo(), JSON.toJSONString(pubLossMes));
                // 微信通知
                final String openId = user.getOpenId();
                final String accessToken = wxUtil.getAccessToken();
                final PubNotify pubNotify = new PubNotify() {{
                    setThing2(new WxEntity(foundThing.getDescription()));
                    setThing1(new WxEntity(foundThing.getName()));
                    setTime3(new WxEntity(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(foundThing.getGmtCreate())));
                    setPhrase4(new WxEntity("待审核"));
                }};
                final Map<String, Object> notifyBody = wxUtil.builderPub(openId, pubNotify);
                wxUtil.postSubMes(accessToken, notifyBody);
                messageService.save(pubLossMes);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
    }
}

package com.example.lostfound.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.dao.MessageMapper;
import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.vo.LossCommentVO;
import com.example.lostfound.entity.TLossComment;
import com.example.lostfound.dao.TLossCommontMapper;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.entity.wxTemplate.PubNotify;
import com.example.lostfound.entity.wxTemplate.WxEntity;
import com.example.lostfound.service.TLossCommontService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.WxUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
public class TLossCommontServiceImpl extends ServiceImpl<TLossCommontMapper, TLossComment> implements TLossCommontService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TLossCommontMapper lossCommontMapper;
    @Autowired
    private TUserService userService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private WxUtil wxUtil;

    private static final String chatTemplateId = "c8ji8CcuH4fCxaVYZnpSl8KR6uPLFyNKZvbnCrKamP8";
    /**
     * 评论自增一
     * @param lossId
     * @return
     */
    @Override
    public Long addCommentNum(Integer lossId) {
        final Long increment = redisTemplate.opsForValue().increment("LOSS_COMMENT_" + lossId, 1);
        return increment;
    }
    @Override
    public Long deleteCommentNum(Integer lossId) {
        final Long increment = redisTemplate.opsForValue().increment("LOSS_COMMENT_" + lossId, -1);
        return increment;
    }
    /**
     * 拿到评论数量
     * @param lossId
     * @return
     */
    @Override
    public Integer getCommentNum(Integer lossId) {
//        final Integer o = (Integer)redisTemplate.opsForValue().get("LOSS_COMMENT_" + lossId);
        final Integer o = lossCommontMapper.selectCount(new QueryWrapper<TLossComment>().eq("lost_thing_id", lossId).eq("type", "1"));
        return o;
    }
    /**
     * 获取所有评论信息
     * @param lossId
     * @return
     */
    @Override
    public List<LossCommentVO> getAllComment(Integer lossId) {
        //拿到丢失详情中所有的一次评论
        final List<TLossComment> tLossCommonts = lossCommontMapper.selectList(new QueryWrapper<TLossComment>().select("id", "comment", "type", "father_id", "user_id","lost_thing_id","father_id", "to_user_name").eq("lost_thing_id", lossId).eq("type","1"));
        if(null == tLossCommonts || tLossCommonts.size()==0) { //为空则说明没有任何评论
            return null;
        }
        //遍历一级评论，找到所有父级id为对应的id的二级评论
        final List<LossCommentVO> commentVOList = tLossCommonts.stream().map(comment -> {
            final List<TLossComment> sonComments = lossCommontMapper.selectList(new QueryWrapper<TLossComment>().select("id", "comment", "type", "father_id", "user_id", "lost_thing_id", "father_id", "to_user_name").eq("lost_thing_id", lossId).eq("type", "2").eq("father_id", comment.getId()));
            //将所有子评论转换为VO
            final List<LossCommentVO> sonLossCommentVOS = convertToCommentVO(sonComments);
            //将父评论转化为vo
            final LossCommentVO flossCommentVO = convertToCommentVO(comment);
            flossCommentVO.setSon(sonLossCommentVOS);

            return flossCommentVO;
        }).collect(Collectors.toList());
       return commentVOList;
    }

    /**
     * 拿到评论点赞数
     * @param mesId
     * @return
     */
    @Override
    public Integer getLikeNum(Integer mesId,Integer lossId) {
        final Integer o = (Integer)redisTemplate.opsForHash().get("LOSS_LIKE_" + lossId, "MES_ID_" + mesId);
        return o;
    }

    /**
     * 设置评论点赞数
     * @param mesId
     * @param lossId
     */
    @Override
    public void setLikes(Integer mesId,Integer lossId) {
        redisTemplate.opsForHash().put("LOSS_LIKE_" + lossId,"MES_ID_" + mesId,0);
    }

    /**
     * 增加评论点赞数
     * @param lossId
     * @param mesId
     * @return
     */
    @Override
    public Long addCommentLikeNums(Integer lossId, Integer mesId) {
        return redisTemplate.opsForHash().increment("LOSS_LIKE_" + lossId,"MES_ID_" + mesId,1);
    }

    @Override
    public List<LossCommentVO> convertToCommentVO(List<TLossComment> lossCommonts) {
        if(null == lossCommonts || lossCommonts.size() < 1) {
            return null;
        }
        final List<LossCommentVO> lossCommentVOS = lossCommonts.stream().map(comment -> {
            final LossCommentVO lossCommentVO = new LossCommentVO();
            //复制
            BeanUtils.copyProperties(comment, lossCommentVO);
            final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, comment.getUserId());
            //设置头像地址
            lossCommentVO.setAddressUrl(userInfoByNameOrId.getAddressUrl());
            //设置昵称
            lossCommentVO.setNickName(userInfoByNameOrId.getNickName());

            return lossCommentVO;
        }).collect(Collectors.toList());
        return lossCommentVOS;
    }

    @Override
    public void commentNotify(TMessage mesVO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                // 线上消息发送
                template.convertAndSend("/exchange/sendToUser/" + mesVO.getToo(), JSON.toJSONString(mesVO));
                // 微信通知
                final TUser user = userService.getOne(new QueryWrapper<TUser>().eq("id", mesVO.getToo()));
                final TUser fromUser = userService.getOne(new QueryWrapper<TUser>().eq("id", mesVO.getFroms()));
                final String openId = user.getOpenId();
                final String accessToken = wxUtil.getAccessToken();
                final PubNotify pubNotify = new PubNotify() {{
                    setThing2(new WxEntity(mesVO.getSendText()));
                    setDate3(new WxEntity(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    setThing4(new WxEntity(fromUser.getNickName()));
                }};
                final Map<String, Object> notifyBody = wxUtil.buildWxMes(openId, pubNotify, chatTemplateId);
                wxUtil.postSubMes(accessToken, notifyBody);
                final int insert = messageMapper.insert(mesVO);
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
    }

    private LossCommentVO convertToCommentVO(TLossComment lossCommonts) {
        if(null == lossCommonts) {
            return null;
        }

        final LossCommentVO lossCommentVO = new LossCommentVO();
        //复制
        BeanUtils.copyProperties(lossCommonts, lossCommentVO);
        final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, lossCommonts.getUserId());
        //设置头像地址
        lossCommentVO.setAddressUrl(userInfoByNameOrId.getAddressUrl());
        //设置昵称
        lossCommentVO.setNickName(userInfoByNameOrId.getNickName());
        return lossCommentVO;
    }



}

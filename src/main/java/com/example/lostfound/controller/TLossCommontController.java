package com.example.lostfound.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.config.websocket.handler.SessionHandler;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.*;
import com.example.lostfound.entity.vo.LossCommentVO;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TLossCommontService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.MesUtil;
import com.example.lostfound.utils.NotifyUtil;
import com.example.lostfound.entity.TMessage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Slf4j
@RestController
@RequestMapping("/api/v1/lcompoment")
@Api(tags = "失物评论接口")
@Validated
public class TLossCommontController extends BaseController{

    @Autowired
    private TLossCommontService lossCommontService;
    @Autowired
    private NotifyUtil notifyUtil;
    @Autowired
    private TLossThingService lossThingService;
    @Autowired
    private TUserService userService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionHandler sessionHandler;

    /**
     * 发布评论
     * @param comment
     * @param lossId
     * @param userId
     * @param type
     * @param fatherId
     * @return
     */
    @GetMapping("/pubcomment")
    public CommonReturnType getCompoment(@RequestParam(value = "com")@NotBlank String comment, //评论内容
                                         @RequestParam(value = "lossId")Integer lossId, //在哪条失物下面评论
                                         @RequestParam(value = "userId")@NotNull Integer userId, //品论人的userid
                                         @RequestParam(value = "type")String type,  //属于几级评论
                                         @RequestParam(value = "fatherId",required = false)Integer fatherId, //评论的fatherid
                                         @RequestParam(value = "toUserId") Integer toUserId
                                         ){
        final TUser nameOrId = userService.getUserInfoByNameOrId(null, toUserId);
        TLossComment lossCommont = new TLossComment() {{
            setComment(comment);
            setType(type);
            setUserId(userId);
            setToUserName(nameOrId.getNickName());
            setFatherId(fatherId ==  0 ? null : fatherId);
            setLostThingId(lossId);
        }};
        final boolean save = lossCommontService.save(lossCommont);
        if(save) {
            final CommonReturnType allComments = getAllComments(lossId);
            if(allComments.getStatus() !="success") {
                //删除数据库中的评论
                lossCommontService.removeById(lossCommont.getId());
                return CommonReturnType.fail("系统错误","评论失败");
            }
            final TLossComment id = lossCommontService.getOne(new QueryWrapper<TLossComment>().eq("lost_thing_id", lossId).last("limit 1"));
            final TLossThing one = lossThingService.getOne(new QueryWrapper<TLossThing>().eq("id", id.getLostThingId()));
            final TUser user = userService.getOne(new QueryWrapper<TUser>().eq("id", one.getLossUserId()));

            // 自己在自己评论区不需要发送消息
            if(userId != toUserId) {
                // 发送异步消息给对方
                final TMessage commentMes = new TMessage();
                commentMes.setFroms(userId);
                commentMes.setLossId(lossId);
                commentMes.setToo(toUserId);
                commentMes.setMsgState("0"); // 默认已读
                commentMes.setSendText(comment);
                commentMes.setTextType("0"); // 文字评论
                commentMes.setType("2"); //聊天通知
//            if(!sessionHandler.isOnline(String.valueOf(toUserId))) {
                log.warn(toUserId + "不在线，开始微信通知");
                final String sessionId = MesUtil.generateSessionId(String.valueOf(userId), String.valueOf(toUserId));
                commentMes.setChatSessionId(sessionId);
                lossCommontService.commentNotify(commentMes);
//            }
            }

            return allComments;
        }
        return CommonReturnType.fail("系统错误","评论失败");
    }

    /**
     * 获取失物的评论
     * @param id
     * @return
     */
    @GetMapping
    public CommonReturnType getAllComments(@RequestParam("id") Integer id) {
        //获取所有评论并转换为vo
        List<LossCommentVO> allComment = lossCommontService.getAllComment(id);
        if(null != allComment && allComment.size() > 0) {
            return CommonReturnType.success(allComment,"获取成功");
        }
        return CommonReturnType.fail(null, "获取失败");
    }

    /**
     * 获取评论数
     * @param id
     * @return
     */
    @GetMapping("/cnum")
    public CommonReturnType getComNum(@RequestParam("lossId") Integer id) {
        Integer commentNum = lossCommontService.getCommentNum(id);
        log.info("第{}件失物评论数量是：{}", id,commentNum);
        if(commentNum == null) {
            commentNum = 0;
        }
        if(null != commentNum && commentNum >=0) {
            return CommonReturnType.success(commentNum,"获取成功");
        }
        return CommonReturnType.fail(null, "获取失败");
    }
    /**
     *
     * @param lossId
     * @return
     *//*
    @GetMapping("/get/like")
    public CommonReturnType getLike(@RequestParam("lossId")Integer lossId) {
        //获取失物下的所有消息id
        final List<TLossCommont> loss_thing_id = lossCommontService.list(new QueryWrapper<TLossCommont>().eq(true, "loss_thing_id", lossId));
        final List<Integer> integerList = loss_thing_id.stream().map(e -> {
             return e.getId();
        }).collect(Collectors.toList());
        if(integerList ==null || integerList.size() < 1) {
            return CommonReturnType.fail(null, "获取失败");
        }
        final List<LossLikesVO> lossLikesVOS = lossCommontService.convertToLikesVO(integerList, lossId);
        return CommonReturnType.success(lossLikesVOS,"获取成功");
    }*/
    /**
     * 评论点赞
     * @param lossId
     * @param mesId
     * @return
     */
    @GetMapping("/like")
    public CommonReturnType like(@RequestParam("lossId")Integer lossId,
                                 @RequestParam("mesId")Integer mesId) {
        final Long aLong = lossCommontService.addCommentLikeNums(lossId, mesId);
        return CommonReturnType.success(aLong);
    }

    /**
     * 取消点赞
     * @param lossId
     * @param mesId
     * @return
     */
    @GetMapping("/dlike")
    public CommonReturnType dlike(@RequestParam("lossId")Integer lossId,
                                 @RequestParam("mesId")Integer mesId) {
        final Long aLong = lossCommontService.addCommentLikeNums(lossId, mesId);
        return CommonReturnType.success(aLong);
    }


}


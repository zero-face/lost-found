package com.example.lostfound.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.*;
import com.example.lostfound.entity.vo.LossCommentVO;
import com.example.lostfound.service.MessageService;
import com.example.lostfound.service.TLossCommontService;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
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
                                         @RequestParam(value = "fatherId",required = false)Integer fatherId //评论的fatherid
                                         ){
        TLossComment lossCommont = new TLossComment() {{
            setComment(comment);
            setType(type);
            setUserId(userId);
            setFatherId(fatherId ==  0 ? null:fatherId);
            setLostThingId(lossId);
        }};
        final boolean save = lossCommontService.save(lossCommont);
        if(save) {
            //评论数量自增1
            final Long num = lossCommontService.addCommentNum(lossId);
            //存入点赞量
            lossCommontService.setLikes(lossCommont.getId(), lossId);
            final HashMap<String, Object> stringObjectHashMap = new HashMap<String,Object>(){{
                put("commentNum",num);
            }};
            final CommonReturnType allComments = getAllComments(lossId);
            if(allComments.getStatus() !="success") {
                //删除品论数
                lossCommontService.deleteCommentNum(lossId);
                //删除数据库中的评论
                lossCommontService.removeById(lossCommont.getId());
                return CommonReturnType.fail("系统错误","评论失败");
            }
            final TLossComment id = lossCommontService.getOne(new QueryWrapper<TLossComment>().eq("lost_thing_id", lossId).last("limit 1"));
            final TLossThing one = lossThingService.getOne(new QueryWrapper<TLossThing>().eq("id", id.getLostThingId()));
            final TUser user = userService.getOne(new QueryWrapper<TUser>().eq("id", one.getLossUserId()));
            final TMessage mesVO = new TMessage();
//            mesVO.setName(one.getName());
            mesVO.setFlag(true);
//            mesVO.setAddressUrl(user.getAddressUrl());
            mesVO.setMessage(comment);
//            mesVO.setNickName(user.getNickName());
            mesVO.setType("2"); //评论消息
            mesVO.setStatus(true);
            mesVO.setFroms(userId);
            mesVO.setToo(one.getLossUserId());
            messageService.save(mesVO);
            if(!userId .equals( user.getId()) || fatherId .equals(userId) ) {
                notifyUtil.publish(JSON.toJSONString(mesVO),one.getLossUserId().toString());
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


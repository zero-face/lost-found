package com.example.lostfound.controller;


import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.LossCommentVO;
import com.example.lostfound.entity.TLossCommont;
import com.example.lostfound.service.TLossCommontService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
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
@RequestMapping("/api/v1/lcompoment")
@Api(tags = "失物评论接口")
@Validated
public class TLossCommontController {

    @Autowired
    private TLossCommontService lossCommontService;
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/pubcomment")
    public CommonReturnType getCompoment(@RequestParam(value = "com")@NotBlank String comment,
                                         @RequestParam(value = "lossId")Integer lossId,
                                         @RequestParam(value = "userId")Integer userId,
                                         @RequestParam(value = "type")String type,
                                         @RequestParam(value = "fatherId",required = false)Integer fatherId,
                                         @RequestParam(value = "likes",required = false)Integer likes){
        TLossCommont lossCommont = new TLossCommont() {{
            setCommont(comment);
            setLikes(likes);
            setType(type);
            setUserId(userId);
            setFatherId(fatherId);
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
            return CommonReturnType.success(stringObjectHashMap,"评论成功");
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
        final List<TLossCommont> allComment = lossCommontService.getAllComment(id);
        if(null != allComment && allComment.size() > 0) {
            final List<LossCommentVO> lossCommentVOS = lossCommontService.convertToCommentVO(allComment);
            return CommonReturnType.success(lossCommentVOS,"获取成功");
        }
        return CommonReturnType.fail(null, "获取失败");

    }

    @GetMapping("/like")
    public CommonReturnType like(@RequestParam("lossId")Integer lossId,
                                 @RequestParam("mesId")Integer mesId) {
        final Long aLong = lossCommontService.addCommentLikeNums(lossId, mesId);
        return CommonReturnType.success(aLong);
    }


}


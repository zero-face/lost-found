package com.example.lostfound.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.entity.LossCommentVO;
import com.example.lostfound.entity.TLossCommont;
import com.example.lostfound.dao.TLossCommontMapper;
import com.example.lostfound.entity.TUser;
import com.example.lostfound.service.TLossCommontService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.service.TLossThingService;
import com.example.lostfound.service.TUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class TLossCommontServiceImpl extends ServiceImpl<TLossCommontMapper, TLossCommont> implements TLossCommontService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TLossCommontMapper lossCommontMapper;
    @Autowired
    private TUserService userService;

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

    /**
     * 拿到评论数量
     * @param lossId
     * @return
     */
    @Override
    public Integer getCommentNum(Integer lossId) {
        final Integer o = (Integer)redisTemplate.opsForValue().get("LOSS_COMMENT_" + lossId);

        return o;
    }

    /**
     * 获取所有评论信息
     * @param lossId
     * @return
     */
    @Override
    public List<TLossCommont> getAllComment(Integer lossId) {
        final List<TLossCommont> tLossCommonts = lossCommontMapper.selectList(new QueryWrapper<TLossCommont>().select("id", "commont", "type", "father_id", "user_id","lost_thing_id","father_id").eq("lost_thing_id", lossId));
        return tLossCommonts;
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
    public List<LossCommentVO> convertToCommentVO(List<TLossCommont> lossCommonts) {
        final List<LossCommentVO> lossCommentVOS = lossCommonts.stream().map(comment -> {
            final LossCommentVO lossCommentVO = new LossCommentVO();
            //复制
            BeanUtils.copyProperties(comment, lossCommentVO);
            //设置点赞量
            lossCommentVO.setLikes(getLikeNum(comment.getId(), comment.getLostThingId()));
            final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, comment.getUserId());
            //设置头像地址
            lossCommentVO.setAddressUrl(userInfoByNameOrId.getAddressUrl());
            //设置昵称
            lossCommentVO.setNickName(userInfoByNameOrId.getNickName());
            return lossCommentVO;
        }).collect(Collectors.toList());
        return lossCommentVOS;
    }

}

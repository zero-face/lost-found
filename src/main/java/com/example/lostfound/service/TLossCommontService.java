package com.example.lostfound.service;

import com.example.lostfound.entity.vo.LossCommentVO;
import com.example.lostfound.entity.TLossCommont;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */

public interface TLossCommontService extends IService<TLossCommont> {
    Long addCommentNum(Integer lossId);
    Long deleteCommentNum(Integer lossId);
    Integer getCommentNum(Integer lossId);
    List<LossCommentVO> getAllComment(Integer lossId);
    Integer getLikeNum(Integer mesId,Integer lossId);
    void setLikes(Integer mesId,Integer lossId);
    Long addCommentLikeNums(Integer lossId,Integer mesId);
    List<LossCommentVO> convertToCommentVO(List<TLossCommont> lossCommonts);

}

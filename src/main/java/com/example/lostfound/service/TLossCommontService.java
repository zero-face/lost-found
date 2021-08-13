package com.example.lostfound.service;

import com.example.lostfound.entity.LossCommentVO;
import com.example.lostfound.entity.TLossCommont;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

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
    Long getCommentNum(Integer lossId);
    List<TLossCommont> getAllComment(Integer lossId);
    Long getLikeNum(Integer mesId,Integer lossId);
    void setLikes(Integer mesId,Integer lossId);
    Long addCommentLikeNums(Integer lossId,Integer mesId);
    List<LossCommentVO> convertToCommentVO(List<TLossCommont> lossCommonts);
}

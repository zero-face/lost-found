package com.example.lostfound.service;

import com.example.lostfound.entity.TMessage;
import com.example.lostfound.entity.vo.AuditVO;
import com.example.lostfound.entity.vo.LossDetailVO;
import com.example.lostfound.entity.vo.LossThingVO;
import com.example.lostfound.entity.TLossThing;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */

public interface TLossThingService extends IService<TLossThing> {
    List<TLossThing> getLossBySearchAndTime(String search,Integer time);
    List<LossThingVO> converToLossVO(List<TLossThing> list);
    LossDetailVO converTOLossDetailVO(TLossThing loss);
    String uploadImage(MultipartFile file);
    AuditVO packageNotifyMes(Integer id,Integer userId,Integer lossId);

    void pubNotify(TMessage pubLossMes, TLossThing lossThing);
}

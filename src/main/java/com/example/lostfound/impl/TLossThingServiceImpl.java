package com.example.lostfound.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostfound.dao.TLossThingMapper;
import com.example.lostfound.entity.*;

import com.example.lostfound.entity.vo.AuditVO;
import com.example.lostfound.entity.vo.LossDetailVO;
import com.example.lostfound.entity.vo.LossThingVO;
import com.example.lostfound.service.TLossCommontService;
import com.example.lostfound.service.TLossThingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lostfound.service.TThingTypeService;
import com.example.lostfound.service.TUserService;
import com.example.lostfound.utils.OSSUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
public class TLossThingServiceImpl extends ServiceImpl<TLossThingMapper, TLossThing> implements TLossThingService {

    @Autowired
    private TLossThingMapper lossThingMapper;
    @Autowired
    private OSSUtil ossUtil;
    @Autowired
    private TLossCommontService lossCommontService;
    @Autowired
    private TUserService userService;
    @Autowired
    private TThingTypeService thingTypeService;

    @Override
    @CachePut(value = "redisCache",key = "'RedisLose' + #search")
    public List<TLossThing> getLossBySearchAndTime(String search, Integer time) {
        final List<TLossThing> tLossThings = lossThingMapper.getLossBySearchAndTime(search, time);
        return tLossThings;
    }

    @Override
    public List<LossThingVO> converToLossVO(List<TLossThing> list) {
        final List<LossThingVO> collect = list.stream().map(vo -> {
            final LossThingVO lossThingVO = new LossThingVO();
            final TUser user = userService.getOne(new QueryWrapper<TUser>().eq(vo.getLossUserId() != null, "id", vo.getLossUserId()));
            //字段拷贝
            BeanUtils.copyProperties(vo, lossThingVO);
            //拿到类型
            final TThingType id = thingTypeService.getById(vo.getType());
            //设置失物类型
            lossThingVO.setType(id.getType());
            lossThingVO.setLossNickName(user.getNickName());
            return lossThingVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public LossDetailVO converTOLossDetailVO(TLossThing loss) {
        if(loss == null) {
            return null;
        }
        final LossDetailVO lossDetailVO = new LossDetailVO();
        BeanUtils.copyProperties(loss,lossDetailVO);
        //获取评论量
        final Integer commentNum = lossCommontService.getCommentNum(loss.getId());
        //获取发布人信息
        final TUser userInfoByNameOrId = userService.getUserInfoByNameOrId(null, loss.getLossUserId());
        //设置发布人昵称
        lossDetailVO.setNickName(userInfoByNameOrId.getNickName());
        //设置评论量
        lossDetailVO.setCommentNum(commentNum);
        //设置发布人头像地址
        lossDetailVO.setAddressUrl(userInfoByNameOrId.getAddressUrl());
        //获取详细类型
        final TThingType id = thingTypeService.getById(loss.getType());
        lossDetailVO.setType(id.getType());
        return lossDetailVO;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if(null == file) {
            return null;
        }
        final String s = ossUtil.uploadImg(file);
        if(s != null) {
            return s;
        }
        return null;
    }

    @Override
    public AuditVO packageNotifyMes(Integer id, Integer userId, Integer lossId) {
        final TLossThing lossThing = lossThingMapper.selectOne(new QueryWrapper<TLossThing>().eq("id", lossId));
        final TUser id1 = userService.getOne(new QueryWrapper<TUser>().eq("id", userId));
        AuditVO auditVO = new AuditVO(){{
            setAddressUrl(id1.getAddressUrl()); //申请人头像
            setLossName(lossThing.getName()); //申请认领物品
            setPicUrl(lossThing.getPictureUrl()); //认领物品图片
            setNickName(id1.getNickName()); //申请人姓名
            setId(id); //设置请求信息id
            setApplyId(userId); //申请人id
        }};
        return auditVO;
    }
}

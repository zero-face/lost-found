package com.example.lostfound.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.entity.LossDetailVO;
import com.example.lostfound.entity.LossThingVO;
import com.example.lostfound.entity.TLossThing;
import com.example.lostfound.service.TLossThingService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.CommandAPDU;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 * 实现的功能主要是首页对于失物的展示(采用分页查询)，失物的分类展示，失物的查询；失物的详细信息查询
 */
@RestController
@RequestMapping("/api/v1/lost")
@Api(tags = "失物模块接口")
public class TLossThingController {

    @Autowired
    private TLossThingService lossThingService;

    /**
     * 首页的分页查询
     * @param pn
     * @return
     */
    @GetMapping("/{pn}")
    @CachePut(value = "redisCache",key = "'RedisLose'+ #pn")
    public CommonReturnType getAllLost(@PathVariable("pn")Integer pn) {
        //查询第pn，每页5条，不查询数据总数
        final Page<TLossThing> page = new Page<>(pn, 5,false);
        final Page<TLossThing> lossThingPage = lossThingService.page(page);
        final List<TLossThing> records = lossThingPage.getRecords();
        if(records != null || records.size() > 0) {
            final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(records);
            return CommonReturnType.success(lossThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");

    }

    /**
     * 失物分类型展示
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    @CachePut(value = "redisCache",key = "'RedisLose'+ #type",condition = "#result!=null")
    public CommonReturnType getLostByType(@PathVariable("type")String type) {
        final List<TLossThing> list = lossThingService.list(new QueryWrapper<TLossThing>().eq("type", type).eq("status", 0));
        if(list == null || list.size() == 0) {
            return CommonReturnType.fail(null,"没有该类型的失物");
        }
        final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(list);
        return CommonReturnType.success(lossThingVOS,"查找成功");
    }

    /**
     * 搜索
     * @param search
     * @param time
     * @return
     */
    @GetMapping
    public CommonReturnType getByFeature(@RequestParam(value = "s",required = true)String search,
                                         @RequestParam(value = "time",required = false)Integer time) {
        final List<TLossThing> lossBySearchAndTime = lossThingService.getLossBySearchAndTime(search, time);
        if (lossBySearchAndTime != null && lossBySearchAndTime.size() > 0) {
            final List<LossThingVO> lossThingVOS = lossThingService.converToLossVO(lossBySearchAndTime);
            return CommonReturnType.success(lossThingVOS,"查询成功");
        }
        return CommonReturnType.fail(null, "查询失败");
    }

    @GetMapping("/detail/{id}")
    public CommonReturnType getDetail(@PathVariable("id")Integer id) {
        final TLossThing byId = lossThingService.getById(id);
        if(byId !=null) {
            final LossDetailVO lossDetailVO = lossThingService.converTOLossDetailVO(byId);

            //评论相关消息
            return CommonReturnType.success(lossDetailVO,"获取成功");
        }
        return CommonReturnType.fail(null, "获取失败");
    }

}


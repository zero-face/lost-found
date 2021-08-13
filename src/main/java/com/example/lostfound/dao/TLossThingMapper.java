package com.example.lostfound.dao;

import com.example.lostfound.entity.TLossThing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Mapper
public interface TLossThingMapper extends BaseMapper<TLossThing> {
    List<TLossThing>  getLossBySearchAndTime(@Param("search")String search,@Param("time")Integer time);
}

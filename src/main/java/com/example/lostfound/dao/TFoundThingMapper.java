package com.example.lostfound.dao;

import com.example.lostfound.entity.TFoundThing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lostfound.entity.TLossThing;
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
public interface TFoundThingMapper extends BaseMapper<TFoundThing> {
    List<TFoundThing> getFoundBySearchAndTime(@Param("search")String search, @Param("time")Integer time, @Param("type") Integer type);

}

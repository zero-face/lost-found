package com.example.lostfound.dao;

import com.example.lostfound.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {
//    TUser test();
//
//    TUser test(@Param("id") Integer id);
//
//    TUser test(@Param("id") Integer id, @Param("name")String name);
}

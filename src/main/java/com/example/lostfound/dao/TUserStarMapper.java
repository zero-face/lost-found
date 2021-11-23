package com.example.lostfound.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lostfound.entity.UserStar;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zero
 * @date 2021/11/23 23:48
 * @description
 * @since 1.8
 **/
@Mapper
public interface TUserStarMapper extends BaseMapper<UserStar> {
}

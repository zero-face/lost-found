package com.example.lostfound.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lostfound.entity.TMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/14 21:54
 * @Since 1.8
 * @Description
 **/
@Mapper
public interface MessageMapper extends BaseMapper<TMessage> {

    List<String> allMySession(@Param("userId")Integer id);

    List<Integer> queryUsersChatMe(@Param("sessionId")String sessionId, @Param("userId")Integer userId);



}

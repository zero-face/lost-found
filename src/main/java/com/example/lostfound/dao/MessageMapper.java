package com.example.lostfound.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lostfound.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Zero
 * @Date 2021/8/14 21:54
 * @Since 1.8
 * @Description
 **/
@Mapper
public interface MessageMapper extends BaseMapper<MessageVO> {
}

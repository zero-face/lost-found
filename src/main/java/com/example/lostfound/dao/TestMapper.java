package com.example.lostfound.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zero
 * @date 2023/3/26 19:37
 * @description
 * @since 1.8
 **/
@Mapper
public interface TestMapper {

    String test();

    boolean bool();

    String test2();

}

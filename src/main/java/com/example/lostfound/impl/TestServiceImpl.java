package com.example.lostfound.impl;

import com.example.lostfound.dao.TestMapper;
import com.example.lostfound.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zero
 * @date 2023/3/26 19:19
 * @description
 * @since 1.8
 **/
@Service
public class TestServiceImpl implements TestService {


    @Autowired
    private TestMapper testMapper;

    /**
     * @param name
     * @return java.lang.String
     * @description
     * @date 2023/3/26 19:19
     * @since 1.8
     **/
    @Override
    public String test(String name) {
        System.out.println(name);
        System.out.println(testMapper.test());
        return "test";
    }

    /**
     * @return boolean
     * @description
     * @date 2023/3/26 19:19
     * @since 1.8
     **/
    @Override
    public boolean bool() {
        return true;
    }

    @Override
    public String test2() {
        return "test2";
    }

    public static void main(String[] args) {
        System.out.println("test");
    }

    @Override
    public void test3() {

        final boolean bool = testMapper.bool();
        System.out.println(bool);
        System.out.println("test3");
    }


    @Override
    public void test4() {
        System.out.println("test4");
    }

    @Override
    public void test5() {
        System.out.println("test5");
    }


}

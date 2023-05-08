package com.example.lostfound;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
//@Import({cn.arros.plugin.core.StartPlug.class})
@MapperScan(basePackages = {"com.example.lostfound.dao"})
@EnableCaching
@CrossOrigin(origins = "*",maxAge = 3600)
public class LostFoundApplication {

    @Value("${test}")
    private String test;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LostFoundApplication.class);
//        Set<ApplicationListener<?>> ls = app.getListeners();
//        SpringAttrsListener asel = new SpringAttrsListener();
//        app.addListeners(asel);


        app.run(args);
        SpringApplication.run(LostFoundApplication.class, args);
    }
    public static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }
}

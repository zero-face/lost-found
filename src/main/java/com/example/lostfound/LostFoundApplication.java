package com.example.lostfound;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
//@Import({cn.arros.plugin.core.StartPlug.class})
@MapperScan(basePackages = {"com.example.lostfound.dao"})
@EnableCaching
public class LostFoundApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LostFoundApplication.class);
//        Set<ApplicationListener<?>> ls = app.getListeners();
//        SpringAttrsListener asel = new SpringAttrsListener();
//        app.addListeners(asel);

        app.run(args);
        SpringApplication.run(LostFoundApplication.class, args);
    }

}

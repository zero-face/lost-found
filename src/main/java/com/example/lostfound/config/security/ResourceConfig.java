package com.example.lostfound.config.security;

import com.example.lostfound.interceptor.JwtIntercepter;
import com.example.lostfound.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author Zero
 * @Date 2021/8/11 4:11
 * @Since 1.8
 * @Description
 **/
@Component
public class ResourceConfig implements WebMvcConfigurer {
    @Autowired
    private JwtIntercepter jwtIntercepter;

    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(repeatSubmitInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(jwtIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/user/login","/api/v1/user/code","/api/v1/lost/list","/**",
                        "/doc.html","/webjars/**","/img.icons/**","/swagger-resources/**","/v2/api-docs",
                        "/swagger-ui.html",
                        "/error","/api/v1/user/test","/api/v1/user/mcode");
    }

}

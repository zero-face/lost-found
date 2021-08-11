package com.example.lostfound.config;

import com.example.lostfound.interceptor.JwtIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Zero
 * @Date 2021/8/11 4:11
 * @Since 1.8
 * @Description
 **/
public class JWTConfig implements WebMvcConfigurer {
    @Autowired
    private JwtIntercepter jwtIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/user/login",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-ui.html",
                        "/error");
    }

}

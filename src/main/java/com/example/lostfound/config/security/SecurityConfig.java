package com.example.lostfound.config.security;

import com.example.lostfound.config.security.handler.*;
import com.example.lostfound.config.security.service.UserNameDetailService;
import com.example.lostfound.config.security.session.CustomizeSessionInformationExpiredStrategy;
import com.example.lostfound.validate.code.ValidateImageCodeFilter;
import com.example.lostfound.validate.smscode.SmsAuthenticationConfig;
import com.example.lostfound.validate.smscode.SmsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.annotation.Resource;


/**
 * @Author Zero
 * @Date 2021/6/3 23:06
 * @Since 1.8
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserNameDetailService userdetailservice;

    @Resource
    private CustomizeAuthenticationEntryPoint customizeAuthenticationEntryPoint;

    @Resource
    private CustomizeSessionInformationExpiredStrategy customizeSessionInformationExpiredStrategy;

    @Resource
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Resource
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

    @Resource
    private CustomizeAccessDeniedHandler customizeAccessDeniedHandler;

    @Resource
    private CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;

    @Resource
    private ValidateImageCodeFilter validateImageCodeFilter;

    @Resource
    private SmsFilter smsFilter;

    @Resource
    private SmsAuthenticationConfig smsAuthenticationConfig;

    /**
     * 自定义数据库查寻认证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userdetailservice).passwordEncoder(passwordEncoder());
    }

    /**
     * 设置加密方式
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置登录
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开启跨域以及关闭防护
        http.csrf().disable().cors();
        http.addFilterBefore(validateImageCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(smsFilter, ValidateImageCodeFilter.class);
        http.apply(smsAuthenticationConfig);
        http.authorizeRequests()
            .antMatchers("/api/v1/user/login","/doc.html","/webjars/**","/img.icons/**",
                    "/swagger-resources/**","/**","/v2/api-docs", "/swagger-ui.html"
                    ,"/api/v1/user/mcode","/api/v1/user/mail","/api/v1/user/image")
            .permitAll()
            .anyRequest().authenticated();
        http.logout()
            .logoutUrl("/api/v1/user/logout").deleteCookies("JSESSIONID")
            .logoutSuccessHandler(customizeLogoutSuccessHandler)
        .and()
            .formLogin()
            .loginProcessingUrl("/api/v1/user/login")
            .successHandler(customizeAuthenticationSuccessHandler)
            .failureHandler(customizeAuthenticationFailureHandler)
        .and()
            .exceptionHandling()
            .accessDeniedHandler(customizeAccessDeniedHandler)
            .authenticationEntryPoint(customizeAuthenticationEntryPoint)
        .and()
            .sessionManagement()
            .maximumSessions(1)
            .expiredSessionStrategy(customizeSessionInformationExpiredStrategy);
    }

    public SmsAuthenticationConfig getSmsAuthenticationConfig() {
        return smsAuthenticationConfig;
    }
}

package com.example.lostfound.validate.smscode;

import com.example.lostfound.config.service.MailDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;


/**
 * @Author Zero
 * @Date 2021/7/4 15:08
 * @Since 1.8
 * @Description TODO
 **/
@Component
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MailDetailService mailDetailService;

    @Override
    public void configure(HttpSecurity http) {
        //一个验证拦截器
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        //给这个验证拦截器设置一个管理器
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置验证成功的处理器
        smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        //设置验证失败的处理器
        smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        //一个验证provider实现验证功能（将权限等信息加进去）
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        //给这个provider设置我的登录账户信息获取service
        smsAuthenticationProvider.setUserDetailService(mailDetailService);
        //将这个验证器加到用户名登录的后面
        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }


}

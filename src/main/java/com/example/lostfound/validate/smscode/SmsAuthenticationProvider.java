package com.example.lostfound.validate.smscode;

import com.example.lostfound.config.service.MailDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author Zero
 * @Date 2021/7/4 15:16
 * @Since 1.8
 * @Description  开始进行验证
 **/

public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MailDetailService mailDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        UserDetails userDetails = mailDetailService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if (null == userDetails) {
            throw new InternalAuthenticationServiceException("未找到与该邮箱对应的用户");
        }
        //标记这个验证结果为已验证
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsService getUserDetailService() {
        return mailDetailService;
    }

    public void setUserDetailService(MailDetailService mailDetailService) {
        this.mailDetailService =  mailDetailService;
    }
}

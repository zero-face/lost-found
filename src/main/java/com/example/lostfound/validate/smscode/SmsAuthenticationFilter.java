package com.example.lostfound.validate.smscode;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Zero
 * @Date 2021/7/4 15:12
 * @Since 1.8
 * @Description TODO 验证前打包
 **/

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String MOBILE_KEY = "mail";

    private String mailParameter = MOBILE_KEY;

    private boolean postOnly = true;


    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/v1/user/mail", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mail = obtainMail(request);

        if (mail == null) {
            mail = "";
        }
        mail = mail.trim();
        //生成一个验证token，但是没有经过验证
        SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mail);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainMail(HttpServletRequest request) {
        return request.getParameter(mailParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mailParameter) {
        Assert.hasText(mailParameter, "mail parameter must not be empty or null");
        this.mailParameter = mailParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mailParameter;
    }
}

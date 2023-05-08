package com.example.lostfound.config.websocket.interceptors;

import cn.hutool.core.collection.CollUtil;
import com.example.lostfound.config.security.dynamicpathaccess.CustomizeFilterInvocationSecurityMetadataSource;
import com.example.lostfound.config.security.service.UserNameDetailService;
import com.example.lostfound.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Zero
 * @date 2022/1/12 0:06
 * @description
 * @since 1.8
 **/
@Component
public class MyChannelInterceptor implements ChannelInterceptor {

    @Value("jwt.tokenHead")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserNameDetailService userNameDetailService;

    @Autowired
    private CustomizeFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //如果当前是连接命令
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            //获取头部token
//            String jwtToken = accessor.getFirstNativeHeader("token");
//            if(jwtToken == null || jwtToken.length() < tokenHead.length()) {
//                throw new IllegalArgumentException("抱歉，您没有访问权限");
//            }
//            jwtToken = jwtToken.substring(tokenHead.length());
//            //校验token，拿取token中的用户信息
//            String username = jwtTokenUtil.getUsername(jwtToken);
//            //拿不到username
//            if (username != null) {
//                UserDetails userDetails = userNameDetailService.loadUserByUsername(username);
//                //验证token是否过期
//                if (jwtTokenUtil.checkIsExpired(jwtToken)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    accessor.setUser(authentication);
//                }
//            }
//            else
//            {
//                throw new IllegalArgumentException("抱歉，您没有访问权限");
//            }
//        }
//        //如果是订阅请求
//        else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            String topic = accessor.getDestination().toString();
//            Authentication authentication = (Authentication)accessor.getUser();
//            System.out.println(topic);
//            //查看是否有这个权限
//            boolean result = checkPermission(authentication, topic);
//            //没有权限
//            if(!result) {
//                // 这里返回一个自定义提示消息，由于没有指令和相关参数，所以不会真正执行订阅
//                Message<String> msg = new Message<String>() {
//                    @Override
//                    public String getPayload() {
//                        return "主题订阅失败";
//                    }
//
//                    @Override
//                    public MessageHeaders getHeaders() {
//                        return null;
//                    }
//                };
//                return msg;
//                // 如果抛出异常，则会导致前端面页连接也会断开，不断重连
//                //throw new IllegalArgumentException("抱歉，没有权限订阅该主题");
//            }
//        }
//        //如果是一个发送消息请求
//        else if(StompCommand.SEND.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            String topic = accessor.getDestination().toString();
//            Authentication authentication = (Authentication)accessor.getUser();
//            //检查是否有权限向该主题发送消息
//            boolean result = checkPermission(authentication, topic);
//            //没有权限
//            if(!result) {
//                throw new IllegalArgumentException("抱歉，没有权限发送消息到该主题");
//            }
//        }
        return message;

    }



    /**
     * 验证当前用户是否有权限订阅主题
     */
    private boolean checkPermission(Authentication authentication, String topic) {

        Collection<ConfigAttribute> configAttributes = filterInvocationSecurityMetadataSource.getAttributes(topic);
        if (CollUtil.isEmpty(configAttributes)) {
            return false;
        }
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            //将访问所需资源或用户拥有资源进行比对
            String needAuthority = configAttribute.getAttribute();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 消息发送立即调用
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    }

    /**
     * 消息发送之后进行调用,是否有异常,进行数据清理
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, @Nullable Exception ex) {
        return;
    }
}
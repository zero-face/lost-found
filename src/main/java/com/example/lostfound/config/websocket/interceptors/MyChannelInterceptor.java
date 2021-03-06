//package com.example.lostfound.config.websocket.interceptors;
//
//import cn.hutool.core.collection.CollUtil;
//import com.example.lostfound.config.security.dynamicpathaccess.CustomizeFilterInvocationSecurityMetadataSource;
//import com.example.lostfound.config.security.service.UserNameDetailService;
//import com.example.lostfound.utils.JwtTokenUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.lang.Nullable;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * @author Zero
// * @date 2022/1/12 0:06
// * @description
// * @since 1.8
// **/
//@Component
//public class MyChannelInterceptor implements ChannelInterceptor {
//
//    @Value("jwt.tokenHead")
//    private String tokenHead;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private UserNameDetailService userNameDetailService;
//
//    @Autowired
//    private CustomizeFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        //???????????????????????????
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            //????????????token
//            String jwtToken = accessor.getFirstNativeHeader("token");
//            if(jwtToken == null || jwtToken.length() < tokenHead.length()) {
//                throw new IllegalArgumentException("??????????????????????????????");
//            }
//            jwtToken = jwtToken.substring(tokenHead.length());
//            //??????token?????????token??????????????????
//            String username = jwtTokenUtil.getUsername(jwtToken);
//            //?????????username
//            if (username != null) {
//                UserDetails userDetails = userNameDetailService.loadUserByUsername(username);
//                //??????token????????????
//                if (jwtTokenUtil.checkIsExpired(jwtToken)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    accessor.setUser(authentication);
//                }
//            }
//            else
//            {
//                throw new IllegalArgumentException("??????????????????????????????");
//            }
//        }
//        //?????????????????????
//        else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            String topic = accessor.getDestination().toString();
//            Authentication authentication = (Authentication)accessor.getUser();
//            System.out.println(topic);
//            //???????????????????????????
//            boolean result = checkPermission(authentication, topic);
//            //????????????
//            if(!result) {
//                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                Message<String> msg = new Message<String>() {
//                    @Override
//                    public String getPayload() {
//                        return "??????????????????";
//                    }
//
//                    @Override
//                    public MessageHeaders getHeaders() {
//                        return null;
//                    }
//                };
//                return msg;
//                // ??????????????????????????????????????????????????????????????????????????????
//                //throw new IllegalArgumentException("????????????????????????????????????");
//            }
//        }
//        //?????????????????????????????????
//        else if(StompCommand.SEND.equals(accessor.getCommand())) {
//            if(message.getPayload().toString().equals("ping")) {
//                return message;
//            }
//            String topic = accessor.getDestination().toString();
//            Authentication authentication = (Authentication)accessor.getUser();
//            //?????????????????????????????????????????????
//            boolean result = checkPermission(authentication, topic);
//            //????????????
//            if(!result) {
//                throw new IllegalArgumentException("?????????????????????????????????????????????");
//            }
//        }
//        return message;
//
//    }
//
//
//
//    /**
//     * ?????????????????????????????????????????????
//     */
//    private boolean checkPermission(Authentication authentication, String topic) {
//
//        Collection<ConfigAttribute> configAttributes = filterInvocationSecurityMetadataSource.getAttributes(topic);
//        if (CollUtil.isEmpty(configAttributes)) {
//            return false;
//        }
//        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
//        while (iterator.hasNext()) {
//            ConfigAttribute configAttribute = iterator.next();
//            //??????????????????????????????????????????????????????
//            String needAuthority = configAttribute.getAttribute();
//            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
//                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * ????????????????????????
//     */
//    @Override
//    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//    }
//
//    /**
//     * ??????????????????????????????,???????????????,??????????????????
//     */
//    @Override
//    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, @Nullable Exception ex) {
//        return;
//    }
//}
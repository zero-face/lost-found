package com.example.lostfound.config.websocket;


import com.example.lostfound.config.websocket.handler.CustomHandshakeHandler;
import com.example.lostfound.config.websocket.handler.CustomHandshakeInterceptor;
import com.example.lostfound.config.websocket.handler.CustomSubProtocolWebSocketHandler;
import com.example.lostfound.config.websocket.interceptors.MyChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.security.Principal;
import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/14 19:45
 * @Since 1.8
 * @Description
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig  extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 配置 WebSocket 进入点，及开启使用 SockJS，这些配置主要用配置连接端点，用于 WebSocket 连接
     *
     * @param registry STOMP 端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").addInterceptors(new CustomHandshakeInterceptor())
                .setHandshakeHandler(new CustomHandshakeHandler()).setAllowedOrigins("*");

//        registry.addEndpoint("/websocket-app").setAllowedOrigins("*");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }

    /**
     * 配置消息代理选项
     *
     * @param registry 消息代理注册配置
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置一个或者多个代理前缀，在 Controller 类中的方法里面发生的消息，会首先转发到代理从而发送到对应广播或者队列中。
//        registry.enableSimpleBroker("/topic/", "/queue/");
        registry.enableStompBrokerRelay("/topic/","/queue/","/exchange/")
                .setRelayHost("39.107.238.203").setRelayPort(61613)
                .setClientLogin("root").setClientPasscode("chl252599")

                .setSystemLogin("root").setSystemPasscode("chl252599")
                .setVirtualHost("/");
        // 配置客户端发送请求消息的一个或多个前缀，该前缀会筛选消息目标转发到 Controller 类中注解对应的方法里
        registry.setApplicationDestinationPrefixes("/app/");
        // 服务端通知特定用户客户端的前缀，可以不设置，默认为user
        registry.setUserDestinationPrefix("/queue/");
    }

    /**
     * 配置通道拦截器，用于获取 Header 的 Token 进行鉴权
     *
     * @param registration 注册通道配置类
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new MyChannelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientOutboundChannel(registration);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        WebSocketMessageBrokerConfigurer.super.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }

    @Bean
    public WebSocketHandler subProtocolWebSocketHandler() {
        return new CustomSubProtocolWebSocketHandler(clientInboundChannel(), clientOutboundChannel());
    }

}

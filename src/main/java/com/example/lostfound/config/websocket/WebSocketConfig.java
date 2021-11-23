package com.example.lostfound.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author Zero
 * @Date 2021/8/14 19:45
 * @Since 1.8
 * @Description
 **/
@Configuration //开启对websocket的支持
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

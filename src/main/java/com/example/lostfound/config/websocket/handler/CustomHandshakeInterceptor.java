package com.example.lostfound.config.websocket.handler;

import com.example.lostfound.config.websocket.userDetail.WebsocketPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author Zero
 * @date 2023/3/19 21:17
 * @description
 * @since 1.8
 **/
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("--websocket的http连接握手之前--");
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        WebsocketPrincipal user = null;
        //获取token认证
        String userId = req.getServletRequest().getHeader("user");
        //解析token获取用户信息
        //鉴权，我的方法是，前端把token传过来，解析token，判断正确与否，return true表示通过，false请求不通过。
        //TODO 鉴权设置用户
        if (StringUtils.isNotBlank(userId)) {
            user = new WebsocketPrincipal(userId);
        }

        //如果token认证失败user为null，返回false拒绝握手
        if (user == null) {
            return false;
        }
        //保存认证用户
        attributes.put("user", user);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}

package com.example.lostfound.config.websocket.userDetail;

import java.security.Principal;

/**
 * @author Zero
 * @date 2023/3/19 21:19
 * @description
 * @since 1.8
 **/
public class WebsocketPrincipal implements Principal {
    private final String name;

    // 函数构造器
    public WebsocketPrincipal(String name) {
        this.name = name;
    }

    // 创建一个私有的构造器
    private WebsocketPrincipal() {
        this.name = null;
    }

    public String getName() {
        return name;
    }
}

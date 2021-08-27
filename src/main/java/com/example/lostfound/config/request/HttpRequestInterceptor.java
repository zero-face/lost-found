/*
package com.example.lostfound.config.request;

import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

*/
/**
 * @Author Zero
 * @Date 2021/8/25 22:45
 * @Since 1.8
 * @Description 统一为所有的请求增加特定的请求头
 **//*

@Configuration
public class HttpRequestInterceptor implements ClientHttpRequestInterceptor {
//    @Autowired
//    private UserMapper userMapper;
    private static final Map<String,String> HEADERS = new HashMap<>(4);

*/
/*    static {
        HEADERS.put("channel","Android");
        HEADERS.put("version","1460");
        HEADERS.put("type","0");
    }*//*


    */
/**
     * 对某些地址的请求增加统一的头和参数
     *//*

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String bodyStr = URLUtil.decode(new String(body));
        String timestamp = String.valueOf(System.currentTimeMillis());

        AntPathMatcher matcher = new AntPathMatcher();
        String path = request.getURI().toString();
        String urlPattern = "https://*.boxkj.com/**";
        if (matcher.match(urlPattern,path)){

            // 除登录请求外都需要token
            String loginPattern = "https://*.boxkj.com/app/appstu/login";
            if (!matcher.match(loginPattern, path)) {
                if (request.getHeaders().get("token") == null) {
                    String username = request.getHeaders().get("username").get(0);
                    QueryWrapper<User> wrapper = new QueryWrapper<>();
                    wrapper.eq("tsn_username", username);
                    User user = userMapper.selectOne(wrapper);
                    request.getHeaders().add("token", user.getTsnToken());
                }
            }

            request.getHeaders().setAll(HEADERS);

            Map<String,String> data = form2Map(bodyStr);
            String sign = TsnUtils.generateSign(timestamp, data);
            data.put("timestamp",timestamp);
            data.put("sign",sign);

            String nBodyStr = URLUtil.encodeQuery(map2Form(data));

            byte[] nBody = nBodyStr.getBytes(StandardCharsets.UTF_8);
            return execution.execute(request, nBody);
        }

        return execution.execute(request, body);
    }

    */
/**
     * 将URL参数转化为Map
     * @param urlParam url参数
     * @return Map
     *//*

    public static HashMap<String, String> form2Map(String urlParam) {
        String[] temp;
        HashMap<String, String> map = new HashMap<>(16);
        temp = urlParam.split("&");
        for(String s : temp) {
            String[] list = s.split("(?<!=)=(?!=)");
            if(list.length>1) {
                map.put(list[0], list[1]);
            }
        }
        return map;
    }

    */
/**
     * 将Map转化为URL参数的形式
     * @param map Map
     * @return url参数字符串
     *//*

    public static String map2Form(Map<String,String> map){
        return map.keySet().stream()
                .map(d -> d + "=" + map.get(d))
                .collect(Collectors.joining("&"));
    }

}

*/

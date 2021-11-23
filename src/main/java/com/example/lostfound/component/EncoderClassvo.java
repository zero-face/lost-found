package com.example.lostfound.component;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/8/14 20:58
 * @Since 1.8
 * @Description
 **/
public class EncoderClassvo implements Encoder.Text<Map<String, List>>{

    @Override
    public void init(EndpointConfig config) {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
    //我向web端传递的是Map类型的
    @Override
    public String encode(Map<String, List> map) throws EncodeException {
        ObjectMapper mapMapper= new ObjectMapper();
        try {
            String json="";
            json=mapMapper.writeValueAsString(map);
            return  json;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }
    /*//如果你传递的是一个类，则使用如下写法
    @Override
    public String encode(Person person) throws EncodeException {
        try {
            return Java2Json.JavaToJson(person, false);
        } catch (MapperException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    */
}
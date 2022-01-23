package com.example.lostfound.entity;

import lombok.Data;

/**
 * @author Zero
 * @date 2022/1/12 0:54
 * @description
 * @since 1.8
 **/
@Data
public class MessageBody {

    private String from;

    private String to;

    private String destination;
}

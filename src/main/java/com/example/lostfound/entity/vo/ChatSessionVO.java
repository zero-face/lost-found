package com.example.lostfound.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Zero
 * @date 2023/3/3 01:12
 * @description
 * @since 1.8
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionVO {

    private String chatSessionId;

    private Integer chatter;

    private String mes;

    private Integer mesId;

    private String type;

    private String picUrl;

    private String userName;

    private Date time;

    private Integer unRead;
}

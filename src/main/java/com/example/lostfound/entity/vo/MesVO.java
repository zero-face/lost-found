package com.example.lostfound.entity.vo;

/**
 * @Author Zero
 * @Date 2021/8/26 17:29
 * @Since 1.8
 * @Description
 **/

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MesVO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //发送者
    private Integer froms;

    private String chatSessionId;

    //接收者
    private Integer too;

    //消息
    private String sendText;

    //标志在线或者离线消息 0 已读 1 未读
    private String msgState;

    // 标记消息：0-文字、表情 1-图片 2-音频 3-位置
    private String textType;

    //消息类型 系统通知-0 聊天-1 评论-2 消息反馈-3
    private String type;

    //发送时间
    private Date gmtCreate;

    private Date gmtModified;

    private String userName;
}
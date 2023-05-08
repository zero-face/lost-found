package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/14 20:50
 * @Since 1.8
 * @Description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_message")
public class TMessage {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //发送者
    private Integer froms;

    private String chatSessionId;

    //接收者
    private Integer too;

    //消息
    private String sendText;

    private Integer lossId;

    //标志在线或者离线消息 0 未读 1 已读
    private String msgState;

    // 标记消息：0-文字、表情 1-图片 2-音频 3-位置 4-订单
    private String textType;

    //消息类型 系统通知-0 聊天-1 评论-2 消息反馈-3 管理员消息-4
    private String type;

    //发送时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}

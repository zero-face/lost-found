package com.example.lostfound.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class MessageVO {
    private String id;
    //发送者
    private Integer froms;
    //接收者
    private Integer too;
    //消息
    private String message;
    //是否定向发送
    private Boolean flag;
    //发送时间
    @TableField(fill = FieldFill.INSERT)
    private Long sendTime;
    //标志在线或者离线消息
    private Boolean status;
    //消息类型
    private String type;
}

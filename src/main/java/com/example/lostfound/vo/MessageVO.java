package com.example.lostfound.vo;

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
    private Long froms;
    //接收者
    private Long too;
    //消息
    private String message;
    //是否定向发送
    private Boolean flag;
    //发送时间
    @TableField(fill = FieldFill.INSERT)
    private Long sendTime;
}

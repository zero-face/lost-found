package com.example.lostfound.entity.vo;

/**
 * @Author Zero
 * @Date 2021/8/26 17:29
 * @Since 1.8
 * @Description
 **/

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_message")
public class MesVO {
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

    private String addressUrl;

    private String nickName;

    private String name;
}
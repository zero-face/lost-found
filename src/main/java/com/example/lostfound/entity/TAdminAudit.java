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

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/14 12:55
 * @Since 1.8
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("t_admin_audit")
public class TAdminAudit {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer lossId;

    private Integer foundId;

    private Integer userId;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private Integer adminId;

    private String des;

    @TableLogic
    private Boolean isDelete;
}

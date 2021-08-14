package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    private Integer userId;

    private Boolean status;

    @TableField(fill = FieldFill.INSERT)
    private Long submitTime;

    private Long auditTime;
}

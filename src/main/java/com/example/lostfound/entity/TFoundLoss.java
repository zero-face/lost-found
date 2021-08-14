package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Zero
 * @Date 2021/8/14 17:27
 * @Since 1.8
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_found_loss")
public class TFoundLoss {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @TableField(fill = FieldFill.INSERT)
    private Long findTime;

    private Integer lossId;

    //审核信息id
    private Integer auditId;
}

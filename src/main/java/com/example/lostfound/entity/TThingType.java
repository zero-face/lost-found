package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/8/14 12:53
 * @Since 1.8
 * @Description
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_thing_type")
public class TThingType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer typeId;

    private String type;
}

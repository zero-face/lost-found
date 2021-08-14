package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/13 19:24
 * @Since 1.8
 * @Description
 **/
@Data
public class LossDetailVO {
    private Integer id;

    private String name;

    private String pictureUrl;

    private String address;

    private String description;

    private Boolean status;

    private Date lossTime;

    private Integer lossUserId;

    private Integer commentNum;

    private String nickName;

    private String addressUrl;

    private String type;

}

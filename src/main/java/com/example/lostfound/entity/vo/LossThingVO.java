package com.example.lostfound.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/13 18:58
 * @Since 1.8
 * @Description
 **/
@Data
public class LossThingVO {
    private Integer id;

    private String name;

    private String pictureUrl;

    private String address;

    private Boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lossTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtCreate;

    private Integer lossUserId;

    private String area;

    private String lossNickName;

    private String type;

    private String description;
}

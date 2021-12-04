package com.example.lostfound.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtCreate;

    private Integer lossUserId;

    private Integer commentNum;

    private String nickName;

    private String addressUrl;

    private String type;

}

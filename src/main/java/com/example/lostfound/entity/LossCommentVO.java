package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Author Zero
 * @Date 2021/8/13 22:32
 * @Since 1.8
 * @Description
 **/
@Data
public class LossCommentVO {

    private Integer id;

    private String commont;

    private String type;

    private Integer fatherId;

    private Integer userId;

    private Integer lostThingId;

    private Long likes;

    private String nickName;

    private String addressUrl;


}

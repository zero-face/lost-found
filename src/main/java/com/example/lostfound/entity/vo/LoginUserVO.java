package com.example.lostfound.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/12 23:26
 * @Since 1.8
 * @Description
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="User视图对象", description="")
public class LoginUserVO {
    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String nickName;

    @ApiModelProperty(value = "用户实名")
    private String trueName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "QQ")
    private String qq;

    @ApiModelProperty(value = "学院")
    private String collage;

    @ApiModelProperty(value = "班级")
    private String clazz;

    @ApiModelProperty(value = "学号")
    private String number;

    @ApiModelProperty(value = "头像地址")
    private String addressUrl;

    @ApiModelProperty(value = "上次登录时间")
    private Date gmtModified;

    private Boolean isTrue;

    private String token;

}

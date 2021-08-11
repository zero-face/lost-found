package com.example.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String nickName;

    @ApiModelProperty(value = "用户实名")
    private String trueName;

    @ApiModelProperty(value = "密码")
    private String password;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastLoginTime;

    @ApiModelProperty(value = "账户是否可用")
    private Boolean enable;
    @ApiModelProperty(value = "账户逻辑删除")
    @TableLogic
    private Boolean isDelete;

    @ApiModelProperty(value = "账户openid")
    private Integer openId;
}

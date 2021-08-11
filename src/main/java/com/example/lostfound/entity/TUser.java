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
@ApiModel(value="TUser对象", description="")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String nickName;

    private String trueName;

    private String password;

    private String sex;

    private String tel;

    private String qq;

    private String collage;

    private String clazz;

    private String number;

    private String addressUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime lastLoginTime;

    private Boolean enable;
    @TableLogic
    private Boolean isDelete;

    private Integer openId;


}

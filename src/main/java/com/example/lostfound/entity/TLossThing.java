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
@ApiModel(value="LossThing对象", description="")
public class TLossThing implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String pictureUrl;

    private String address;

    private Long loseTime;

    private String description;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Long lossTime;

    @TableLogic
    private Boolean isDelete;

    private Integer lossUserId;


}

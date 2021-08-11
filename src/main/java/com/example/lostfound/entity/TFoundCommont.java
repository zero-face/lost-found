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
@ApiModel(value="FoundCommont对象", description="")
public class TFoundCommont implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String commont;

    @TableField(fill = FieldFill.INSERT)
    private Long foundTime;

    private Integer type;

    @TableLogic
    private Boolean isDelete;

    private Integer fatherId;

    private Integer userId;

    private Integer foundThingId;


}

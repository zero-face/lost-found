package com.example.lostfound.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Zero
 * @Date 2021/8/14 17:55
 * @Since 1.8
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TFoundThingVO {

    private Integer id;

    private String name;

    private String pictureUrl;

    private String address;

    private Long loseTime;

    private String description;

    private Integer status;

    private Long publishTime;

    private Integer publishUserId;

    private String type;

}

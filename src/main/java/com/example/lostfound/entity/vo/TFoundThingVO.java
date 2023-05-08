package com.example.lostfound.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private String nickName;

    private Integer userId;

    private String userAddress;

    private String pictureUrl;

    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date loseTime;

    private String description;

    private Boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtCreate;

    private Integer publishUserId;

    private String type;

}

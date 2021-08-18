package com.example.lostfound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/8/18 2:22
 * @Since 1.8
 * @Description
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LossLikesVO {

    private Integer lossId;
    private Integer mesId;
    private Integer likes;
}

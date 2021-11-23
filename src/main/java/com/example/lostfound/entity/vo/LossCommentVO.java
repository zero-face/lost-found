package com.example.lostfound.entity.vo;

import lombok.Data;

import java.util.List;

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

    private Integer likes;

    private String nickName;

    private String addressUrl;

    private List<LossCommentVO> son;




}

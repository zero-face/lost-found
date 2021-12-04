package com.example.lostfound.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
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

    private String comment;

    private String type;

    private Integer fatherId;

    private Integer userId;

    private Integer lostThingId;

    private String nickName;

    private String addressUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtCreate;

    private List<LossCommentVO> son;
}

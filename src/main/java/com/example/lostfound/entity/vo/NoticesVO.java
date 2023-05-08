package com.example.lostfound.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Zero
 * @date 2023/5/4 15:30
 * @description
 * @since 1.8
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticesVO {
    private String chatSessionId;

    private Integer froms;

    private Integer too;

    private String mes;

    private Integer lossId;

    private String lossUrl;

    private Integer mesId;

    private String type;

    private String picUrl;

    private String userName;

    private Date time;
}

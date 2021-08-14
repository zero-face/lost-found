package com.example.lostfound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/8/14 14:17
 * @Since 1.8
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuditVO {
    private Integer id;

    private String addressUrl;

    private String nickName;

    private String lossName;

    private String picUrl;
}

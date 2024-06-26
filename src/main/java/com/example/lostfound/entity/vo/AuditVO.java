package com.example.lostfound.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

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

    /**
     * 请求信息id
     */
    private Integer id;

    /**
     * 申请人头像
     */
    private String addressUrl;

    /**
     * 申请人名称
     */
    private String nickName;

    /**
     * 失物名
     */
    private String lossName;

    /**
     * 失物头像
     */
    private String picUrl;

    private String des;

    private String status;

    /**
     * 管理员id
     */
    private Integer adminId;

    /**
     * 申请人失物ID
     */
    private Integer applyId;

    private Integer userId;

    /**
     * 申请时间
     */
    private Date gmtCreate;

    private Date gmtModified;
}

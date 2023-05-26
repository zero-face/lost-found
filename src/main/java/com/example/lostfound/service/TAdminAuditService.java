package com.example.lostfound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lostfound.entity.TAdminAudit;
import com.example.lostfound.entity.TMessage;

/**
 * @Author Zero
 * @Date 2021/8/14 13:13
 * @Since 1.8
 * @Description
 **/
public interface TAdminAuditService extends IService<TAdminAudit> {
    void checkNotify(Integer lossId, TMessage messageVO, Integer type, Boolean flag);
}

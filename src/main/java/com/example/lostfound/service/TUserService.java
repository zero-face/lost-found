package com.example.lostfound.service;

import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-08-10
 */
public interface TUserService extends IService<TUser> {
    Boolean checkUsername(String username);
    Integer getUserIdByUserName(String username);
    Boolean modifyUserInfo(TUser user,String oldUsername);
    Boolean checkImageCode(String code,String username);
    String getOpenIdByCode(String url, Map<String,String> map) throws BusinessException, IOException;
    TUser getUserInfoByNameOrId(String username,Integer id);
}

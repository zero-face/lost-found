package com.example.lostfound.config.security.dynamicpathaccess;

import com.example.lostfound.entity.Role;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author Zero
 * @Description 实现一个安全源数据，来获取请求路径的所有权限
 * @Date 2021/6/23 22:06
 * @Since 1.8
 **/

@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {



    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //查询具体某个接口的权限
        List<Integer> permissionIds = null;

        if(null == permissionIds || permissionIds.size() == 0){
            //请求路径没有配置权限，表明该请求接口可以任意访问
            return null;
        }
        //hack
        List<Role> permissionList = null;
        permissionList = permissionIds
                .stream()
                .map(permissionByPathId -> {
                    //hack
                    Role permission = null;

                    return permission;
                }).collect(Collectors.toList());
        String[] attributes = new String[permissionList.size()];
        for(int i = 0; i < permissionList.size(); i++){
            //hack
            attributes[i] = permissionList.get(i).toString();
        }
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

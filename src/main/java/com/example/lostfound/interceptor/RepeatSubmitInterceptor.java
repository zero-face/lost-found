package com.example.lostfound.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.lostfound.annotation.RepeatSubmit;
import com.example.lostfound.core.response.CommonReturnType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Zero
 * @date 2021/12/26 0:17
 * @description 每一次都会进入，判定是否带有重复提交的注解
 * @since 1.8
 **/
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截到的请求时方法处理器
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            //方法上带有防止重复提交的注解
            if (annotation != null) {
                //如果是重复提交
                if (this.isRepeatSubmit(request, annotation)) {
                    CommonReturnType ajaxResult = CommonReturnType.fail(null,annotation.message());
                    response.getWriter().write(JSONObject.toJSONString(ajaxResult));
                    return false;
                }
            }
            return true;
        }
        else {
            return true;
        }
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
}

package com.example.lostfound.aspectj;

import com.example.lostfound.annotation.RateLimiter;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.CommonError;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.core.response.CommonReturnType;
import com.example.lostfound.enums.LimitType;
import com.example.lostfound.utils.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * @author Zero
 * @date 2021/12/26 14:06
 * @description 限流处理
 * @since 1.8
 **/
@Component
@Aspect
public class RateLimiterAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint joinPoint, RateLimiter rateLimiter) throws BusinessException {
        //限制key前缀
        final String key = rateLimiter.key();
        //时间
        final long time = rateLimiter.time();
        //次数
        final int count = rateLimiter.count();
        final String combineKey = getCombineKey(rateLimiter, joinPoint);
        limitFlow(combineKey, time, count);

    }
    private void limitFlow(String key, long time, long count) throws BusinessException {
        long cur = System.currentTimeMillis();
        if(redisTemplate.hasKey(key)) {
            System.out.println(cur-time + "===" + cur);
            final Integer requestCount = redisTemplate.opsForZSet().rangeByScore(key, cur - time, cur).size();
            System.out.println(requestCount);
            if(null != requestCount && requestCount >= count) {
                throw new BusinessException(EmBusinessError.RATELIMITOE_ERROR, "请求速度过快，请稍后重试！");
            }
        }
        redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(),cur);
        final Long aLong = redisTemplate.opsForZSet().removeRangeByScore(key, 0, cur - time);
        log.error("请求速度过快，请稍后重试,已删除{}条数据",aLong);
    }

    private String getCombineKey(RateLimiter rateLimiter, JoinPoint joinPoint) {
        final StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        //根据IP限流
        if (rateLimiter.limitType() == LimitType.IP) {
            //key后面跟上ip
            stringBuffer.append(IPUtil.getIpAddr(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest())).append("-");
        }
        if(rateLimiter.limitType() == LimitType.USER) {
            //跟上用户名
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            stringBuffer.append(userDetails.getUsername()).append("-");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final Class<?> declaringClass = method.getDeclaringClass();
        //后面跟上类名，方法名
        stringBuffer.append(declaringClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}

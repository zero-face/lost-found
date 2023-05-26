package com.example.lostfound.aspectj;

import com.example.lostfound.annotation.RateLimiter;
import com.example.lostfound.core.error.BusinessException;
import com.example.lostfound.core.error.EmBusinessError;
import com.example.lostfound.enums.LimitType;
import com.example.lostfound.utils.ip.AddressUtils;
import com.example.lostfound.utils.ip.IPUtil;
import org.aspectj.lang.JoinPoint;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
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

    /**
     * 限流实现
     * 运用redis的zset结构，以唯一请求标识为key，随机字符串为value，当前时间戳位权重
     * 算法：
     * 拼接出当前请求的唯一combineKey，然后查看redis中以这个combineKey为key的并且权重是在当前（时间-time）的数量，
     * 如果超过预设值说明超过流量限制，抛出提示性异常；否则则写入该请求
     * 支持ip限流，用户名限流；用redis中的key区别；
     * @param key 存放在Redis的唯一标识，全局限流默认是自定义key加上类名+方法名；IP限流则添加了ip，用户限流则添加了用户名
     * @param time 规定时间范围
     * @param count 规定时间内的请求数量
     * @throws BusinessException 自定义异常类，用于抛出限流异常
     */
    private void limitFlow(String key, long time, long count) throws BusinessException {
        long cur = System.currentTimeMillis();
        if(redisTemplate.hasKey(key)) {
            final Integer requestCount = redisTemplate.opsForZSet().rangeByScore(key, cur - time, cur).size();
            if(null != requestCount && requestCount >= count) {
                final String ip = IPUtil.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
                log.error(AddressUtils.getRealAddressByIP(ip) + " ip地址: " + ip +"请求速度过快，请稍后重试");
                throw new BusinessException(EmBusinessError.RATELIMITOE_ERROR);
            }
        }
        redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(),cur);
        final Long aLong = redisTemplate.opsForZSet().removeRangeByScore(key, 0, cur - time);
    }

    private String getCombineKey(RateLimiter rateLimiter, JoinPoint joinPoint) {
        final StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        //根据IP限流
        if (rateLimiter.limitType() == LimitType.IP) {
            // key后面跟上ip
            stringBuffer.append(IPUtil.getIpAddr(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest())).append("-");
        }
        if(rateLimiter.limitType() == LimitType.USER) {
            // 跟上用户名
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            stringBuffer.append(userDetails.getUsername()).append("-");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final Class<?> declaringClass = method.getDeclaringClass();
        // 后面跟上类名，方法名
        stringBuffer.append(declaringClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}

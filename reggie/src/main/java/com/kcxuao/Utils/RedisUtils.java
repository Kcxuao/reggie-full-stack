package com.kcxuao.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtils {


    public static RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    public RedisTemplate<Object, Object> setRedis(RedisTemplate<Object,Object> redisTemplate) {
        return RedisUtils.redisTemplate = redisTemplate;
    }

    public static void createRedis(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
    }

    public static void createRedis(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public static void removeRedis(String key) {
        redisTemplate.delete(key);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}

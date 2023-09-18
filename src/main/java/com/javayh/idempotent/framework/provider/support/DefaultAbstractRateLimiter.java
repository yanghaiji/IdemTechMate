package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractRateLimiter;
import com.javayh.idempotent.framework.core.properties.RateLimiterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限流桶
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-14
 */
@Slf4j
public class DefaultAbstractRateLimiter implements AbstractRateLimiter {

    @Autowired
    private RateLimiterProperties rateLimiterProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final static Map<String, Integer> LIMIT_DURATION_CACHE = new ConcurrentHashMap<>();
    private final static Map<String, Long> COUNT_CACHE = new ConcurrentHashMap<>();
    private final static Map<String, Integer> REQUEST_DURATION_CACHE = new ConcurrentHashMap<>();

    /**
     * 数据初始化
     */
    @Override
    @PostConstruct
    public void init() {
        rateLimiterProperties.getUriConfig().values().forEach(o -> {
            List<String> bucketKey = o.getBucketKey();
            bucketKey.forEach(key -> {
                LIMIT_DURATION_CACHE.put(key, o.getLimitDuration());
                COUNT_CACHE.put(key, o.getCount());
                REQUEST_DURATION_CACHE.put(key, o.getRequestDuration() * 1000);
            });
        });

    }

    /**
     * 是否在限流的列表
     *
     * @param key 热key
     * @return 是否存在
     */
    @Override
    public boolean allowRequest(String key) {
        String redisKey = convert(key) + ":" + System.currentTimeMillis() / REQUEST_DURATION_CACHE.get(key);
        Long count = redisTemplate.opsForValue().increment(redisKey);

        if (Objects.nonNull(count) && count == 1) {
            // 第一次访问，设置过期时间
            redisTemplate.expire(redisKey, LIMIT_DURATION_CACHE.get(key), TimeUnit.SECONDS);
        }
        Long qps = COUNT_CACHE.get(key);
        // FIXME: 2023/9/18  多活时这里存在bug
        return count > qps;
    }

    private String convert(String key) {
        return key.replaceAll("/", ":").replaceFirst(":", "");
    }

}

package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractRateLimiter;
import com.javayh.idempotent.framework.core.properties.RateLimiterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final static Map<String, Long> QPS_CACHE = new ConcurrentHashMap<>();

    /**
     * 数据初始化
     */
    @Override
    public void init() {
        long currentTimeMillis = System.currentTimeMillis();
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        rateLimiterProperties.getUriConfig().values().forEach(o -> {
            List<String> bucketKey = o.getBucketKey();
            bucketKey.forEach(key -> {
                LIMIT_DURATION_CACHE.put(key, o.getLimitDuration());
                QPS_CACHE.put(key, o.getQps());
                zSetOperations.add(key, currentTimeMillis, currentTimeMillis);
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
        long currentTimeMillis = System.currentTimeMillis();

        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        // 监视令牌桶的有序集合
        redisTemplate.watch(key);
        // 开始Redis事务
        redisTemplate.multi();
        Integer limitDuration = LIMIT_DURATION_CACHE.get(key);
        // 获取过期的时间戳
        long expireTimestamp = currentTimeMillis - limitDuration;

        // 获取令牌桶中所有分数小于过期时间戳的成员
        Set<Object> expiredMembers = zSetOperations.rangeByScore(key, Double.MIN_VALUE, (double) expireTimestamp);

        // 删除这些过期的成员
        if (!CollectionUtils.isEmpty(expiredMembers)) {
            for (Object member : expiredMembers) {
                zSetOperations.remove(key, member);
            }
            long currentRequests = zSetOperations.zCard(key);
            if (currentRequests < QPS_CACHE.get(key)) {
                zSetOperations.add(key, String.valueOf(currentTimeMillis), currentTimeMillis);
                // 提交Redis事务
                List<Object> results = redisTemplate.exec();
                if (CollectionUtils.isEmpty(results)) {
                    return true;
                }
            }
        }

        // 回滚Redis事务
        redisTemplate.discard();
        return false;
    }

}

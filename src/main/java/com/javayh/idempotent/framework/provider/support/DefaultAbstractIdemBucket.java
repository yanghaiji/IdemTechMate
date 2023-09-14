package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractIdemBucket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * redis 实现接口幂等的数据存储
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-12
 */
public class DefaultAbstractIdemBucket implements AbstractIdemBucket {

    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultAbstractIdemBucket(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 判断是否存在缓存数据
     *
     * @param key 唯一标识
     * @return 返回true标识存在，注解返回错误
     */
    @Override
    public boolean isEmpty(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 将数据存储到桶内
     *
     * @param key   唯一标识
     * @param value 存储的内容
     */
    @Override
    public void putBucket(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据key 删除
     *
     * @param key 唯一标识
     */
    @Override
    public void delBucket(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 给指定的key设置过期时间
     *
     * @param key    唯一标识
     * @param expire 过期时间 秒为单位
     */
    @Override
    public void expire(String key, int expire) {
        if (expire > 0) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

}

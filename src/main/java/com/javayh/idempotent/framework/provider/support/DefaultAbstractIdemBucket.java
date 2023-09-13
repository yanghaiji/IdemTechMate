package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractIdemBucket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    Map<String, Object> cache = new ConcurrentHashMap<>();

    /**
     * 判断是否存在缓存数据
     *
     * @param key 唯一标识
     * @return 返回true标识存在，注解返回错误
     */
    @Override
    public boolean isEmpty(String key) {
        return cache.containsKey(key);
    }

    /**
     * 将数据存储到桶内
     *
     * @param key   唯一标识
     * @param value 存储的内容
     */
    @Override
    public void putBucket(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 根据key 删除
     *
     * @param key 唯一标识
     */
    @Override
    public void delBucket(String key) {
        cache.remove(key);
    }

    /**
     * 给指定的key设置过期时间
     *
     * @param key    唯一标识
     * @param expire 过期时间
     */
    @Override
    public void expire(String key, int expire) {

    }

}

package com.javayh.idempotent.framework.core;

/**
 * <p>
 * 限流桶
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-14
 */
public interface AbstractRateLimiter {

    /**
     * 数据初始化
     */
    void init();

    /**
     * 是否在限流的列表
     *
     * @param key 热key
     * @return 是否存在
     */
    boolean allowRequest(String key);

}

package com.javayh.idempotent.framework.core;

/**
 * <p>
 * 接口存储桶
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
public interface AbstractIdemBucket {

    /**
     * 判断是否存在缓存数据
     *
     * @param key 唯一标识
     * @return 返回true标识存在，注解返回错误
     */
    boolean isEmpty(String key);

    /**
     * 将数据存储到桶内
     *
     * @param key   唯一标识
     * @param value 存储的内容
     */
    void putBucket(String key, Object value);

    /**
     * 根据key 删除
     *
     * @param key 唯一标识
     */
    void delBucket(String key);

    /**
     * 给指定的key设置过期时间
     *
     * @param key    唯一标识
     * @param expire 过期时间
     */
    void expire(String key, int expire);

    /**
     * 进行key 的拼接
     *
     * @param key 唯一标识
     * @return 将通过 {@link AbstractKeyGenerate#generateKey(String)} 生成的key 转换成符合k.v 存储的规范
     */
    default String convertKey(String key) {
        return key.replaceAll(" ", ":");
    }


}

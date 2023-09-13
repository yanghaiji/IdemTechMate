package com.javayh.idempotent.framework;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 存储的信息
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdemTechValue implements Serializable {

    /**
     * 访问的key
     */
    private String key;

    /**
     * 访问者
     */
    private String user;

    /**
     * 访问的时间
     */
    private Long time;

    /**
     * 访问的url
     */
    private String url;

    /**
     * 过期时间
     */
    private Integer expireTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

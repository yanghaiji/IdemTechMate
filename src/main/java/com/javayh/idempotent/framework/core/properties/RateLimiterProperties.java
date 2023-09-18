package com.javayh.idempotent.framework.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 统一限流的策略配置
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "idem.limiter")
public class RateLimiterProperties {

    /**
     * 是否开启日志
     */
    private Boolean showLog = false;

    /**
     * 单个接口的限流，高于全局的配置
     */
    private Map<String, Limiter> uriConfig;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Limiter {

        /**
         * 最大的qps数据
         * <p>
         * maxQps = {@link RateLimiterProperties#getUriConfig()#getCount()} / {@link RateLimiterProperties#getUriConfig()#getRequestDuration()}
         */
        private Long count;

        /**
         * 请求的持续时间，默认 1s
         * <p>
         * 用来计算最终的qps数据量
         */
        private Integer requestDuration = 1;

        /**
         * 被限流的时常，单位为秒，默认时10s
         */
        private Integer limitDuration = 10;

        /**
         * 被限流的接口
         */
        private List<String> bucketKey;

    }

}

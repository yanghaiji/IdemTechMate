package com.javayh.idempotent.framework.configuration;

import com.javayh.idempotent.framework.core.interceptor.IdempotentInterceptor;
import com.javayh.idempotent.framework.core.properties.RateLimiterProperties;
import com.javayh.idempotent.framework.provider.endoint.IdemTechMateEndpoint;
import com.javayh.idempotent.framework.provider.support.DefaultAbstractIdemBucket;
import com.javayh.idempotent.framework.provider.support.DefaultAbstractKeyGenerate;
import com.javayh.idempotent.framework.provider.support.DefaultAbstractUserInfoContent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Configuration
@Import({DefaultAbstractKeyGenerate.class, IdemTechMateEndpoint.class,
        WebIdemTechMateConfiguration.class, IdempotentInterceptor.class,
        DefaultAbstractIdemBucket.class, DefaultAbstractUserInfoContent.class,
        RedisConfiguration.class, RateLimiterProperties.class})
public @interface IdemTechMateAutoConfiguration {
}

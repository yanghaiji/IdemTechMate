package com.javayh.idempotent.framework.configuration;

import com.javayh.idempotent.framework.provider.endoint.IdemTechMateEndpoint;
import com.javayh.idempotent.framework.provider.support.UidGenerateSupport;
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
@Import({UidGenerateSupport.class, IdemTechMateEndpoint.class})
public @interface IdemTechMateAutoConfiguration {
}

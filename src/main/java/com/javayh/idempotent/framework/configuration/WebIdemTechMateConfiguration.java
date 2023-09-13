package com.javayh.idempotent.framework.configuration;

import com.javayh.idempotent.framework.core.interceptor.IdempotentInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author haiji
 */
public class WebIdemTechMateConfiguration implements WebMvcConfigurer {

    private final IdempotentInterceptor idempotentInterceptor;

    public WebIdemTechMateConfiguration(IdempotentInterceptor idempotentInterceptor) {
        this.idempotentInterceptor = idempotentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idempotentInterceptor)
                .addPathPatterns("/**").excludePathPatterns("/**/idem/key");
    }
}

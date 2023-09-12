package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.provider.core.AbstractIdGenerate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
@Component
public class UidGenerateSupport implements AbstractIdGenerate {

    /**
     * 生成 id
     */
    @Override
    public String generateId(String prefix) {
        prefix = StringUtils.isEmpty(prefix) ? "idem" : prefix;
        return prefix + " " + UUID.randomUUID().toString();
    }
}

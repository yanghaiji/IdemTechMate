package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractKeyGenerate;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * <p>
 * 默认的id 生成器
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
public class DefaultAbstractKeyGenerate implements AbstractKeyGenerate {

    /**
     * 生成 id
     */
    @Override
    public String generateKey(String prefix) {
        prefix = StringUtils.isEmpty(prefix) ? "idem" : prefix;
        return prefix + " " + UUID.randomUUID().toString();
    }
}

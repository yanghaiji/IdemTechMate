package com.javayh.idempotent.framework.provider.support;

import com.javayh.idempotent.framework.core.AbstractUserInfoContent;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-12
 */
public class DefaultAbstractUserInfoContent implements AbstractUserInfoContent {

    /**
     * 获取当前的登录人
     */
    @Override
    public String getUser() {
        return "Default";
    }
}

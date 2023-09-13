package com.javayh.idempotent.framework.example;

import com.javayh.idempotent.framework.core.AbstractUserInfoContent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用于测试 其他实现时，将其放在第一位执行，即覆盖某人的实现
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-12
 */
@Primary
@Component
public class CustomAbstractUserInfoContent implements AbstractUserInfoContent {

    /**
     * 获取当前的登录人
     */
    @Override
    public String getUser() {
        return "Custom";
    }
}

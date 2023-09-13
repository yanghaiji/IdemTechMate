package com.javayh.idempotent.framework.provider.endoint;

import com.javayh.idempotent.framework.provider.support.DefaultAbstractKeyGenerate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 服务端生成的全局唯一id生成器，作为限流的基础
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
@RestController
public class IdemTechMateEndpoint {

    private final DefaultAbstractKeyGenerate generateSupport;

    public IdemTechMateEndpoint(DefaultAbstractKeyGenerate generateSupport) {
        this.generateSupport = generateSupport;
    }

    /**
     * 服务端唯一标识
     */
    @GetMapping(value = "idem/key")
    public String gen(String prefix) {
        return generateSupport.generateKey(prefix);
    }

}

package com.javayh.idempotent.framework.example;

import com.javayh.idempotent.framework.annotation.Idempotent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-12
 */
@Slf4j
@RestController
@RequestMapping(value = "test")
public class IdemController {

    @Idempotent
    @GetMapping("/query")
    public String idem() {
        log.info("数据处理...");
        return "idem tech mate";
    }
}

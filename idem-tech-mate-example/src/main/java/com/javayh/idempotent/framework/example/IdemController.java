package com.javayh.idempotent.framework.example;

import com.javayh.idempotent.framework.annotation.Idempotent;
import com.javayh.idempotent.framework.core.properties.RateLimiterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    RateLimiterProperties properties;

    @Idempotent
    @GetMapping("/query")
    public String idem() {
        log.info("数据处理...");
        return "idem tech mate";
    }

    @Idempotent
    @GetMapping("/tech")
    public String tech() {
        properties.getUriConfig().forEach((k,v)->{
            log.info(k,v);
            List<String> bucketKey = v.getBucketKey();
            log.info("在 {},中 Qps配置为 {}",k,v.getQps());
            log.info("在 {},中 LimitDuration {}",k,v.getLimitDuration());
            log.info("在 {},中 BucketKey配置为 {}",k, Arrays.toString(bucketKey.toArray()));
        });
        log.info("数据处理...");
        return "idem tech mate";
    }
}

package com.javayh.idempotent.framework.provider.core;

/**
 * <p>
 * id生成器
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-09-11
 */
public interface AbstractIdGenerate {

    /**
     * 生成 id
     *
     * @param prefix 添加自定义前缀 ，如果为空，默认给 Idem
     * @return 返回值格式为 ：Idem 28639a95-0c10-468f-8126-ee2f59960612,
     * <p>
     * 且必循遵守测个是， prefix xxx, 后续的解析中将空格替换成 : 作为key,
     * <p>
     * 格式 idem:28639a95-0c10-468f-8126-ee2f59960612
     */
    String generateId(String prefix);
}

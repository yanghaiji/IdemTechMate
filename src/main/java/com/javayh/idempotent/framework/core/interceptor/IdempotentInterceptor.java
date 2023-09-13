package com.javayh.idempotent.framework.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.javayh.idempotent.framework.IdemTechValue;
import com.javayh.idempotent.framework.annotation.Idempotent;
import com.javayh.idempotent.framework.core.AbstractIdemBucket;
import com.javayh.idempotent.framework.core.AbstractUserInfoContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


/**
 * 目前考虑通过拦截器实现，主要考虑以下几点
 * <p>
 * 模块化和分离关注点： 拦截器的主要作用是在请求进入控制器之前或之后进行预处理或后处理，而AOP则是一种更广泛的横切关注点的方式。
 * 将幂等性处理逻辑放在拦截器中有助于将幂等性检查与其他关注点（如日志记录、安全性检查等）分离开来，使代码更模块化和易于维护。
 * <p>
 * 精确控制： 拦截器可以精确地控制哪些请求需要进行幂等性检查，哪些不需要。这种精确控制有助于避免不必要的性能开销，只在需要时应用幂等性检查。
 * <p>
 * 更好的可扩展性： 如果你的应用需要在将来引入更多的预处理或后处理逻辑，将这些逻辑添加到拦截器中会更容易管理和扩展。AOP可能会导致切面逻辑变得复杂，难以维护。
 * <p>
 * 统一处理： 拦截器可以被应用于整个应用程序，确保所有请求都受到相同的幂等性检查。这有助于维护一致的行为，降低错误的风险。
 * <p>
 * 清晰可见： 幂等性处理通常是请求处理的一个关键步骤，将其放在拦截器中可以使这一重要逻辑在代码中更加清晰可见，提高代码的可读性。
 *
 * @author haiji
 */
@Slf4j
public class IdempotentInterceptor implements HandlerInterceptor {

    private final AbstractIdemBucket idemBucket;
    private final AbstractUserInfoContent userInfoContent;

    public IdempotentInterceptor(AbstractIdemBucket idemBucket,
                                 AbstractUserInfoContent userInfoContent) {
        this.idemBucket = idemBucket;
        this.userInfoContent = userInfoContent;
    }

    /**
     * 进行请求的与处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return getPreHandle(request, response, handler);
    }


    /**
     * postHandle: 这个方法在请求处理器（Controller 方法）执行之后，在视图渲染之前执行。它通常用于对模型数据进行进一步处理或记录日志等操作。
     * 如果在 preHandle 中返回 false 或抛出异常，则 postHandle 不会被执行。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * afterCompletion: 这个方法在请求完成之后，包括视图渲染完成后执行。它无论请求处理成功与否都会执行。
     * 如果在 preHandle 中返回 false 或抛出异常，afterCompletion 也会被执行，但此时的 response 对象可能已经被修改。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 执行完成后可以直接删除key
    }


    private Boolean getPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(Idempotent.class)) {
                Idempotent idempotent = method.getAnnotation(Idempotent.class);
                int expire = idempotent.expire();
                String token = request.getHeader("Idem");
                String convertKey = idemBucket.convertKey(token);
                IdemTechValue idemTechValue = IdemTechValue.builder()
                        .user(userInfoContent.getUser()).time(System.currentTimeMillis())
                        .key(convertKey).url(request.getRequestURI()).expireTime(expire).build();
                if (!idemBucket.isEmpty(convertKey)) {
                    // 放入缓存
                    idemBucket.putBucket(convertKey, idemTechValue);
                    // 设置过期时间
                    idemBucket.expire(convertKey, expire);
                    log.info("访问系统的日志 {}", idemTechValue.toString());
                    return true;
                } else {
                    log.info("访问系统的被幂等后的日志 {}", idemTechValue.toString());
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    // 设置响应的内容类型为 JSON
                    response.setContentType("application/json;charset=UTF-8");
                    // 构建要返回的 JSON 消息
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", HttpServletResponse.SC_CONFLICT);
                    jsonObject.put("msg", "执行成功");
                    // 获取响应输出流并将 JSON 写入其中
                    response.getWriter().write(jsonObject.toJSONString());
                    // 不要忘记关闭输出流
                    response.getWriter().flush();
                    response.getWriter().close();
                    return false;
                }
            }
        }
        return true;
    }

}

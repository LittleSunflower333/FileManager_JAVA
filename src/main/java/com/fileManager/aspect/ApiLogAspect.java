package com.fileManager.aspect;

import com.fileManager.entity.ApiLog;
import com.fileManager.service.IApiLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fileManager.service.IUsersService;
import com.fileManager.util.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class ApiLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLogAspect.class);

    @Autowired
    private IApiLogService apiLogService;
    @Autowired
    private IUsersService usersService;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* com.fileManager.controller..*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取当前请求的URL
        String url = request.getRequestURL().toString();

        // 排除 ApiLogController 中的 /list 接口
        if (url.contains("/apiLog/")) {
            // 如果是该接口，不记录日志，直接执行目标方法
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        ApiLog apiLog = new ApiLog();
        String username = "unknown";

        if (UserContext.getCurrentUser() != null) {
            username = usersService.getUserById(UserContext.getCurrentUser()).getUsername();
        }

        try {
            // 获取请求的相关信息
            String url_ = request.getRequestURL().toString();
            String ip = request.getRemoteAddr();
            Object[] args = joinPoint.getArgs();
            String methodName = joinPoint.getSignature().toShortString();

            apiLog.setUrl(url_);
            apiLog.setIp(ip);
            apiLog.setMethod(methodName);
            apiLog.setParams(Arrays.toString(args));
            apiLog.setCreatedAt(LocalDateTime.now());
            apiLog.setUsername(username);

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 方法执行成功记录
            long endTime = System.currentTimeMillis();
            apiLog.setExecutionTime(endTime - startTime);
            apiLog.setStatus(1);

            // 对返回结果进行简化
            String simplifiedResult = simplifyResult(result);
            apiLog.setResult(simplifiedResult);

            logger.info("API调用成功: {}", apiLog);
            return result;

        } catch (Throwable throwable) {
            // 方法执行异常记录
            apiLog.setStatus(0);
            apiLog.setError(throwable.getMessage());
            logger.error("API调用异常: {}", apiLog, throwable);
            throw throwable;
        } finally {
            // 将日志存储到数据库
            apiLogService.save(apiLog);
        }
    }

    /**
     * 简化返回结果，避免记录过长的信息
     */
    private String simplifyResult(Object result) {
        if (result == null) {
            return "null";
        }

        // 如果结果是一个复杂对象，可以记录对象的一部分或一些关键字段
        if (result instanceof String) {
            return (String) result; // 直接返回字符串
        } else if (result instanceof List || result instanceof Map) {
            // 对集合或映射的简化：记录集合的大小或首个元素
            return "Size: " + ((Collection<?>) result).size();
        } else {
            // 对其他对象进行 JSON 序列化，但限制最大长度
            try {
                String jsonResult = new ObjectMapper().writeValueAsString(result);
                // 截取前500个字符，避免记录过长
                return jsonResult.length() > 500 ? jsonResult.substring(0, 500) : jsonResult;
            } catch (Exception e) {
                return "Error serializing result";
            }
        }
    }

}


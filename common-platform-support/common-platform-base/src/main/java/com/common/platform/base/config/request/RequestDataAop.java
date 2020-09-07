package com.common.platform.base.config.request;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(500)
public class RequestDataAop {
    @Pointcut("execution(* *..controller.*.*(..))")
    public void cutService() {}

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
        Object result;
        try {
            result = point.proceed();
        } finally {
            RequestDataHolder.remove();
        }
        return result;
    }
}


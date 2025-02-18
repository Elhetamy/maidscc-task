package com.maidscc.maidscc_task.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.maidscc.maidscc_task.service.*.*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.maidscc.maidscc_task.controller.*.*(..))")
    public void controllerMethods() {}

    @Around("serviceMethods() || controllerMethods()")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Executing {}.{}", className, methodName);

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Completed {}.{} in {} ms", className, methodName, (endTime - startTime));
            return result;
        } catch (Exception ex) {
            log.error("Exception in {}.{}: {}", className, methodName, ex.getMessage());
            throw ex;
        }
    }
}
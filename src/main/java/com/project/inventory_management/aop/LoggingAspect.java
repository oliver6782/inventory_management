package com.project.inventory_management.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.project.inventory_management.controller.*.*(..))")
    public Object logControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Execution time of {} with args {} :: {} ms",
                joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()), elapsedTime);

        return result;
    }

}


package com.warpfuture.iot.api.enterprise.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogRecord;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut(value = "execution(public * com.warpfuture.iot.api.enterprise.service.*.*(..))")
    public void logServicePointcut() {
    }

    @Pointcut(value = "execution(public * com.warpfuture.iot.api.enterprise.controller.*.*(..))")
    public void logControllerPointcut() {
    }

//    @Pointcut(value = "execution(public * com.warpfuture.iot.api.enterprise.authfilter.TokenAuthFilter.doFilter(..))")
//    public void filterPointcut() {
//    }
//
//    @Around(value = "filterPointcut()")
//    public void filterSurrounding(ProceedingJoinPoint proceedingJoinPoint) {
//        log(proceedingJoinPoint, "Filter Before:--", null);
//        Object proceed = null;
//        try {
//            proceed = proceedingJoinPoint.proceed();
//            log(proceedingJoinPoint, "Filter After:--", proceed);
//        } catch (Throwable e) {
//            logger.error("Filter Error Found: ", e);
//        }
//        log(proceedingJoinPoint, "Filter Returning:--", proceed);
//    }

    @Before(value = "logServicePointcut()")
    public void beforeService(JoinPoint joinPoint) {
        log(joinPoint, "Service Before", null);
    }

    @After(value = "logServicePointcut()")
    public void afterService(JoinPoint joinPoint) {
        log(joinPoint, "Service After", null);
    }

    @AfterReturning(value = "logServicePointcut()", returning = "result")
    public void returnService(JoinPoint joinPoint, Object result) {
        log(joinPoint, "Service Returning", result);
    }

    @AfterThrowing(value = "logServicePointcut()", throwing = "throwable")
    public void throwService(Throwable throwable) {
        logger.error("Service Error Found: ", throwable);
    }

    @Before(value = "logControllerPointcut()")
    public void beforeController(JoinPoint joinPoint) {
        log(joinPoint, "Controller Before", null);
    }

    @After(value = "logControllerPointcut()")
    public void afterController(JoinPoint joinPoint) {
        log(joinPoint, "Controller After", null);
    }

    @AfterReturning(value = "logControllerPointcut()", returning = "result")
    public void returnController(JoinPoint joinPoint, Object result) {
        log(joinPoint, "Controller Returning", result);
    }

    @AfterThrowing(value = "logControllerPointcut()", throwing = "throwable")
    public void throwController(Throwable throwable) {
        logger.error("Controller Error Found: ", throwable);
    }

    private void log(JoinPoint joinPoint, String time, Object result) {
        String method = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        String typeName = joinPoint.getSignature().getDeclaringTypeName();

        String join = StringUtils.join(args, ", ");

        logger.info("\nEnterprise: Time: {} {}: {}.{}(args: {}) return {}", LocalDateTime.now(), time, typeName, method, join, result);
    }

}

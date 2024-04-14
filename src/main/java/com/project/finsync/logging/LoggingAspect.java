package com.project.finsync.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Before("execution(* com.project.finsync.service.*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String params = Arrays.toString(joinPoint.getArgs());

        logger.log(Level.INFO, "Method {0} of class {1} called with parameters: {2}", new Object[]{methodName, className, params});
    }

    @AfterThrowing(pointcut = "execution(* com.project.finsync.service.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.log(Level.SEVERE, "Exception thrown in method {0} of class {1}: {2}", new Object[]{methodName, className, ex.getMessage()});
    }
}

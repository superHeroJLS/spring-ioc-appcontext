package com.springaop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 使用AspectJ定义一个aspect，用来实现一个log的AOP
 * Spring也提供其他方法来装配AOP，但都没有使用AspectJ注解的方式来得简洁明了
 */
@Aspect
@Component
public class LoggingAspect {

    // 在执行UserService的每个方法前执行:
    @Before("execution(public * com.springaop.service.UserService.*(..))")
    public void doAccessCheck() {
        System.err.println("[Before] do access check...");
    }
    
    // 在执行MailService的每个方法前后执行:
    @Around("execution(public * com.springaop.service.MailService.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println("[Around] start " + pjp.getSignature());
        Object retVal = pjp.proceed();// 调用真正的业务方法
        System.err.println("[Around] done " + pjp.getSignature());
        return retVal;
    }
}

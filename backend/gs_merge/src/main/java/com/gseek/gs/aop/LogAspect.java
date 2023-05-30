package com.gseek.gs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用aop打印debug日志.
 * config下的类不能使用aop,要自己写debug日志.
 *
 * @author Phak
 * @since 2023/5/28-19:48
 */
@Aspect
@Slf4j
@Component
public class LogAspect {


    /*
     * @Pointcut("execution(* com.gseek.gs.config..*.*(..))"
     * 会使tomcat不能正常启动
     */
    @Pointcut(
        "execution(* com.gseek.gs.controller..*.*(..))" +
        "|| execution(* com.gseek.gs.service..*.*(..))" +
        "|| execution(* com.gseek.gs.pojo..*.*(..))" +
        "|| execution(* com.gseek.gs.exce..*.*(..))" +
        "|| execution(* com.gseek.gs.util..*.*(..))" +
        "|| execution(* com.gseek.gs.websocket..*.*(..))" +
        "|| execution(* com.gseek.gs.common..*.*(..)) "
    )
    public void log() {}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("类 {} 的方法 {} 将开始执行",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName() );
        List<Object> args = List.of(joinPoint.getArgs());
        for (Object object : args){
            log.debug("参数类型为 {} ,值为 {} ", object.getClass().getSimpleName(), object);
        }
    }

    @After("log()")
    public void doAfter(JoinPoint joinPoint) {
        log.debug("方法 {} 开始执行", joinPoint.getSignature().getName() );
    }

    @AfterReturning(pointcut = "log()", returning = "returnValue")
    public void doReturning(JoinPoint joinPoint, Object returnValue) {
        log.debug("方法 {} 成功执行结束,返回值为:{}",
                joinPoint.getSignature().getName(),
                returnValue
                );
    }

    @AfterThrowing(pointcut="log()", throwing="exception")
    public void doThrowing(JoinPoint joinPoint, Throwable exception) {
        log.debug("方法 {} 抛出异常,异常信息:\n{}",
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }
}

package com.hand.custom.annotation.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author tianmin.wang
 * @version date：2019年9月16日 下午2:13:10
 * 
 */
@Order(1)
@Aspect
@Component
public class LogCustomAspect {
	@Pointcut("@annotation(com.hand.custom.annotation.LogCustom)")
	public void controllerAspect() {
	}

	@Before("controllerAspect()")
    public void doAfterReturn(JoinPoint joinPoint){
        System.out.println("==========后置通知=========");
    }
}

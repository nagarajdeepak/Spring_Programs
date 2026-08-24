package com.example.studentmarks.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class loggingaspect {

	@Before("execution(* com.example.studentmarks.service.studentservice.*(..))")
	public void beforMethod(JoinPoint joinpoint) {
		System.out.println("================================");
        System.out.println("AOP: Method execution started");
        System.out.println("Method: " + joinpoint.getSignature().getName());
        System.out.println("================================");
	}
	
	@After("execution(* com.example.studentmarks.service.studentservice.*(..))")
	public void afterMethod(JoinPoint joinpoint) {
		System.out.println("================================");
        System.out.println("AOP: Method execution completed");
        System.out.println("Method: " + joinpoint.getSignature().getName());
        System.out.println("================================");
	}
}
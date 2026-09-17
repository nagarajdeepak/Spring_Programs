package com.example.logistics.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Cross-cutting request logging via Spring AOP, so individual controllers
 * don't need to log entry/exit/timing themselves.
 * The pointcut matches every public method on every class in the controller package.
 */

@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {
	
	@Around("execution(public * com.example.logistics.controller..*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.currentTimeMillis();
		String signature  = joinPoint.getSignature().toShortString();
		log.info(">> {}", signature);
		try {
			Object result = joinPoint.proceed();
			log.info("<< {} complete in {} ms", signature, System.currentTimeMillis() - start);
			return result;
		} catch (Throwable ex) {
			log.warn("<<{} failed after {}ms: {}", signature, System.currentTimeMillis() - start, ex.getMessage());
			throw ex;
		}
	}
}
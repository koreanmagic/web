package kr.co.koreanmagic.test.classes.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AspectJTEST {
	
	Logger logger = Logger.getLogger("test");
	
	@Pointcut("execution(* hello(..))")
	public void hello() {
		
	}
	
	@Around("execution(* *(..))")
	public Object aroundTest(ProceedingJoinPoint join) throws Throwable {
		return "고정철";
	}

}

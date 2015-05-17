package kr.co.koreanmagic.web2.support.argresolver.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({PARAMETER})
@Retention(RUNTIME)
public @interface Service {
	
	String value() default "";
	String path() default "";
	String param() default "";
}

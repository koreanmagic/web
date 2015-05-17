package kr.co.koreanmagic.web2.support.argresolver.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({PARAMETER})
@Retention(RUNTIME)
public @interface BoardList {
	
	String name() default "boardList";
	String orderBy() default "<insertTime";
	int page() default 1;
	int size() default 25;
	int linkLen() default 10;
	
}

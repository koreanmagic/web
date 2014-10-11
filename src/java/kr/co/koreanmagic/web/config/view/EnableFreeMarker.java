package kr.co.koreanmagic.web.config.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FreeMarkerConfiguration.class)
public @interface EnableFreeMarker {
	int order() default 1;
	
}

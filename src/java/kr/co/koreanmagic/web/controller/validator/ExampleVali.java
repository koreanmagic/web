package kr.co.koreanmagic.web.controller.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;


//@ConstraintComposition(CompositionType.AND)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExampleValidator.class)
public @interface ExampleVali {
	String message() default ""; // "{JSR-303 검증 예제 : ExampleVali 랼루랼루}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

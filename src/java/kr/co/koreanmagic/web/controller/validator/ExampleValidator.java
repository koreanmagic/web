package kr.co.koreanmagic.web.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
 * 	ConstraintValidator<A, T>  ▶  A : 어노테이션 // T : 어노테이션이 붙을 필드 혹은 메서드 리턴값의 클래스 타입 
 */
public class ExampleValidator implements ConstraintValidator<ExampleVali, String> {

	
	
	/*
	 *  어노테이션을 인자로 받는다.
	 *  어노테이션에 붙어있는 메타정보를 떼어내어, 인스턴스 필드에 넣어둘때 사용한다.
	 */
	@Override
	public void initialize(ExampleVali anno) {
		
	}

	
	@Override
	public boolean isValid(String fieldValue, ConstraintValidatorContext context) {
		return false;
	}

}

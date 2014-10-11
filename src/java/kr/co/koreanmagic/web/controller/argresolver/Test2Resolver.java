package kr.co.koreanmagic.web.controller.argresolver;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class Test2Resolver implements HandlerMethodArgumentResolver {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		logger.debug(parameter);
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		logger.debug(parameter);
		return null;
	}

}

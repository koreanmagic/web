package kr.co.koreanmagic.web2.support.argresolver;

import static kr.co.koreanmagic.web2.support.argresolver.Utils.cast;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web2.controller.RequestInfo;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/*
 * RequestInfo를 꺼내준다.
 */
@Component
public class RequestInfoResolver implements HandlerMethodArgumentResolver {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(RequestInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest req  = cast(webRequest);
		return req.getAttribute(RequestInfo.REQUEST_KEY);
		
	}
}

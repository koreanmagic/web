package kr.co.koreanmagic.web2.support.argresolver;

import static kr.co.koreanmagic.web2.support.argresolver.Utils.cast;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.koreanmagic.web.support.nav.Navigator;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class NavigatorResolver implements HandlerMethodArgumentResolver {
	
	Logger logger = Logger.getLogger(getClass());
	
	/*
	 * 메소드의 파라미터를 검사해서 지원여부를 확인한다.
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(Navigator.class);
	}

	
	/*
	 *	해당 파라미터는 resolveArgument의 반환값으로 대체된다.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
									NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest req  = cast(webRequest);
		HttpSession session = req.getSession();
		return session.getAttribute("kr.co.koreanmagic.web.support.nav.Navigator");
	}
	

}

package kr.co.koreanmagic.web2.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/*
 * 일반적으로 쓰이는 데이터를 request Attribute에 미리 심어놓는다.
 */
@Component
public class GeneralWebDataInterceptor implements HandlerInterceptorRegister {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//request.setAttribute("servletPath", request.getServletPath());
		
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	@Override
	public String[] getPathPatterns() {
		return null;
	}
}

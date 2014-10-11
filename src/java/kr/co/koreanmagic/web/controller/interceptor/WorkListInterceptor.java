package kr.co.koreanmagic.web.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.web.support.WorkListProcessor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*
 * 현재 위치를 알려주는 인터셉터
 */
@Component
public class WorkListInterceptor extends HandlerInterceptorImpl {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired private WorkListProcessor BEAN_PROCESSOR;
	
	
	private void init(HttpServletRequest request) {
		request.setAttribute("beanProcessor", BEAN_PROCESSOR); // 빈 프로세서
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		init(request);
		return true;
	}

	@Override
	public String[] getPathPatterns() {
		return new String[]{"/worklist/**"};
	}
}

package kr.co.koreanmagic.web.controller.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

public interface HandlerInterceptorRegister extends HandlerInterceptor {
	
	// 요청패턴을 적용함. null을 보내면 모든 요청에 응답
	String[] getPathPatterns();
}

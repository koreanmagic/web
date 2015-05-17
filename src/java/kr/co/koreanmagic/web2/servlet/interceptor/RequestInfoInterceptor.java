package kr.co.koreanmagic.web2.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.web2.controller.RequestInfo;
import kr.co.koreanmagic.web2.controller.admin.support.AdminRequestInfo;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/*
 * 각 페이지별로 들어갈 글로벌 정보
 * URL구조
 * /admin/분류(work, partner, address)/{페이지정보}/이후는 알아서~
 */
@Component
public class RequestInfoInterceptor implements HandlerInterceptorRegister {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		/*
		 * 사용처)) tag.IncludeJS
		 */
		String[] names = request.getServletPath()
									.substring(1)	 // 처음에 있는 /는 없앤다.
									.split("/");
		
		request.setAttribute(RequestInfo.REQUEST_KEY, new AdminRequestInfo(names));
		
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
		// TODO Auto-generated method stub
		return null;
	}

}

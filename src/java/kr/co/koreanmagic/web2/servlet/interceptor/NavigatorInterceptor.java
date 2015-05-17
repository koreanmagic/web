package kr.co.koreanmagic.web2.servlet.interceptor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.koreanmagic.web.support.nav.Navigator;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class NavigatorInterceptor implements HandlerInterceptorRegister {

	private Logger logger = Logger.getLogger(getClass());
	@Inject Provider<Navigator> navigatorFactory;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Navigator navigator = (Navigator)session.getAttribute(Navigator.class.getName());
		if(navigator == null) {
			navigator = navigatorFactory.get();
			session.setAttribute(Navigator.class.getName(), navigator);
		}
		
		navigator.reflesh(request);
		
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

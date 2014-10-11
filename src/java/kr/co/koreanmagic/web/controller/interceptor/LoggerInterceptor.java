package kr.co.koreanmagic.web.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggerInterceptor extends HandlerInterceptorImpl {
	
	Logger logger = Logger.getLogger("webLog");

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug(request.getServletPath() + " [" 
						+ request.getRemoteHost() + ":" + request.getRemotePort() + "] / " + request.getSession().getId());
		return true;
	}

	@Override
	public String[] getPathPatterns() {
		return null;
	}

}

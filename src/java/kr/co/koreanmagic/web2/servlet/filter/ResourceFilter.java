package kr.co.koreanmagic.web2.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ResourceFilter implements Filter {
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String fileName = changeFileName((HttpServletRequest)request);
		
		if(fileName != null) {
			((HttpServletResponse)response).setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		}
		
		chain.doFilter(request, response);
	}
	
	private String changeFileName(HttpServletRequest request) {
		return null;
	}

	@Override
	public void destroy() {
	}

}

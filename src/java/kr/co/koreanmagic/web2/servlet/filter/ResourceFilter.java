package kr.co.koreanmagic.web2.servlet.filter;

import java.io.IOException;
import java.net.URLEncoder;

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
		
		/*
		 * HTTP Header는 아스키코드만으로 이루어져야 한다.
		 * 때문에 fileName이 한글일 경우 문제가 발생한다. 따라서 이를 URL인코딩 해주어야 한다.
		 * 또한 인코딩시, 공백문자가 +로 변경되는 문제가 있다.
		 * 이를 위해 인코딩된 문자를 다시 한번 replace 해준다. 
		 */
		String fileName = resolveFileName((HttpServletRequest)request);
		
		((HttpServletResponse)response)
				.setHeader("Content-Disposition",
						String.join("", "attachment; filename=\"", URLEncoder.encode(fileName, "utf-8").replace("+", "%20"), "\"") );
	
		chain.doFilter(request, response);
	}
	
	private String resolveFileName(HttpServletRequest request) {
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		
		if(name == null) {
			name = getFileName( request.getServletPath() );
		}
		
		if( type != null ) {
			switch(type) {
				// refer일 경우 뒤에 붙어있는 해시를 지운다.
				case "refer":
					return name.replaceFirst("-[^-]+(?=\\.\\w+$)", "");
				default :
					return name;
			}
		}
		
		return name;
	}
	
	// ServletPath에서 파일명 추출하기
	private String getFileName( String servletPath ) {
		return servletPath.substring( servletPath.lastIndexOf("/") + 1, servletPath.length() );
	}

	@Override
	public void destroy() {
	}

}

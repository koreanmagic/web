package kr.co.koreanmagic.web.servlet.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.web.domain.WorkDraft;
import kr.co.koreanmagic.web.domain.WorkFile;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.support.WorkListProcessor;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class WorkResourceUpdate implements Filter {

	private Logger logger = Logger.getLogger(getClass());
	
	private static WebApplicationContext context;
	private static WorkListProcessor workListProcessor;
	private String servletDisplayName;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.servletDisplayName = filterConfig.getInitParameter("servletName");
	}

	@Override
	public void doFilter(ServletRequest _request, ServletResponse _response,
			FilterChain chain) throws IOException, ServletException {
		
		boolean isAct = false;
		
		HttpServletRequest request = (HttpServletRequest)_request;
		if(context == null) init(request);
		
		String query = request.getQueryString();
		if (query != null) {
			// id 쿼리가 있다는 얘기는 파일을 직접적으로 요청한다는 뜻이다. 파일 데이터베이스를 업데이트 해준다.
			if (query.lastIndexOf("id=") != -1) {
				String id = getId(query);
				String fileType = getFileType(query);
				count(fileType, Integer.valueOf(id)); // 업데이트
				isAct = true;
			}
		}
		
		HttpServletResponse response = null;
		if (isAct) { // 다운로드 창이 뜨게 한다.
			response = (HttpServletResponse) _response;
			response.setHeader("Content-Disposition", "attachment");
			//response.setContentType("application/octet-stream");
		}
		chain.doFilter(request, _response);
		
	}
	
	private void init(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		context = (WebApplicationContext)servletContext.getAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + this.servletDisplayName);
		workListProcessor = context.getBean(WorkListProcessor.class);
	}
	
	private void count(String fileType, Integer id) {
		if(fileType.equals(WorkFile.class.getSimpleName()))
			workListProcessor.workFileCountUp(id);
		if(fileType.equals(WorkDraft.class.getSimpleName()))
			workListProcessor.workDraftCountUp(id);
		if(fileType.equals(WorkResource.class.getSimpleName()))
			workListProcessor.workResourceCountUp(id);
	}	
	
	private static final String ALL_STRING = "([a-zA-z가-힣0-9]+)";
	private static final Pattern FILE_TYPE = Pattern.compile("fileType=" + ALL_STRING);
	private static final Pattern ID = Pattern.compile("id=" + ALL_STRING);
	
	private String getFileType(String query) {
		return matcher(FILE_TYPE.matcher(query));
	}
	private String getId(String query) {
		return matcher(ID.matcher(query));
	}
	
	private String matcher(Matcher m) {
		if(m.find())
			return m.group(1);
		else
			return null;
	}

	@Override
	public void destroy() {
	}

}

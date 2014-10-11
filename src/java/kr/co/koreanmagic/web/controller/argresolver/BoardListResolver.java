package kr.co.koreanmagic.web.controller.argresolver;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.koreanmagic.web.controller.support.GenericBoardList;
import kr.co.koreanmagic.web.support.WorkBoardList;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/*
 * 스프링이 처리하고 남은 파라미터를 넘겨주는 것 같ㄴ다. 
 */

@Component
public class BoardListResolver implements HandlerMethodArgumentResolver {

	Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(BoardListSupport.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession();
		WorkBoardList boardList = null;
		
		if(session.getAttribute("boardList") != null) { // 세션에 있으면 그걸 가지고 온다.
			boardList = (WorkBoardList)session.getAttribute("boardList");
			boardList.init(); // 초기화
		} else { 
			boardList = new WorkBoardList();
			session.setAttribute("boardList", boardList);
		}
		
		setParams(webRequest, boardList);
		
		boardList.setPath(request.getServletPath());		// 서블릿 패스 입력
		boardList.setQueryString(queryString(request.getQueryString()));	// 서블릿 쿼리 입력
		
		return boardList;
	}
	
	private static final String size = "&?size=\\d+&?";
	private static final String page = "&?page=\\d+&?";
	// size와 page 쿼리를 지워서 보낸다.
	private String queryString(String servletQuery) {
		if(servletQuery == null) return servletQuery;
		servletQuery = servletQuery.replaceAll(size, ""); // size쿼리 지움
		servletQuery = servletQuery.replaceAll(page, ""); // page쿼리 지움
		return servletQuery;
	}
	
	// 보드리스트에 넣고 남는 쿼리를 돌려준다.
	private void setParams(NativeWebRequest webRequest, GenericBoardList<?> boardList) {
		Object param = null;
		if((param = webRequest.getParameter("size")) != null)
			boardList.setSize(Integer.valueOf((String)param));
		if((param = webRequest.getParameter("page")) != null)
			boardList.setPage(Integer.valueOf((String)param));
	}
	
	

}


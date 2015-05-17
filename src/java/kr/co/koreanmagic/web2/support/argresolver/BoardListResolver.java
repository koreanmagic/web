package kr.co.koreanmagic.web2.support.argresolver;

import static kr.co.koreanmagic.web2.support.argresolver.Utils.cast;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web2.support.argresolver.annotation.BoardList;
import kr.co.koreanmagic.web2.support.paging.GenericBoardList;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class BoardListResolver implements HandlerMethodArgumentResolver {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(BoardList.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
									NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		BoardList paging = parameter.getParameterAnnotation(BoardList.class);
		HttpServletRequest req  = cast(webRequest);
		ModelMap model = mavContainer.getModel();
		
		// 제원확인
		String orderBy = req.getParameter("order") == null ? paging.orderBy() : req.getParameter("order");
		int page = req.getParameter("page") == null ? paging.page() : Integer.parseInt(req.getParameter("page")),
			size = req.getParameter("size") == null ? paging.size() : Integer.parseInt(req.getParameter("size"));
		
		GenericBoardList pagingObj = new GenericBoardList( paging.linkLen() );
		
		pagingObj.setPath(req.getServletPath());	// 패스입력
		pagingObj.setOrder(orderBy);				// 정렬방식 입력
		model.put(paging.name(), pagingObj.set(page, size) );
		
		return pagingObj;
	}

}

package kr.co.koreanmagic.web2.support.argresolver;

import static kr.co.koreanmagic.web2.support.argresolver.Utils.cast;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web.support.board.BoardList;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Board;

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
		return parameter.hasParameterAnnotation(Board.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
									NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Board paging = parameter.getParameterAnnotation(Board.class);
		HttpServletRequest req  = cast(webRequest);
		ModelMap model = mavContainer.getModel();
		
		// 제원확인
		BoardList<?> boardList = null;
		
		String orderBy = req.getParameter("order") == null ? paging.orderBy() : req.getParameter("order");
		
		int page = req.getParameter("page") == null ? paging.page() : Integer.parseInt(req.getParameter("page")),
			size = req.getParameter("size") == null ? paging.size() : Integer.parseInt(req.getParameter("size")),
			links = paging.linkLen();
			
		
			boardList = (BoardList<?>)parameter
								.getParameterType()
								.getConstructor(int.class, int.class, int.class, String.class, String.class)
								.newInstance( links,
													size,
													page,
													orderBy,
													req.getServletPath()
												);
		if( req.getQueryString() != null )
			boardList.addQuery( req.getParameterMap() );
		
		model.put(paging.name(), boardList );
		
		return boardList;
	}

}

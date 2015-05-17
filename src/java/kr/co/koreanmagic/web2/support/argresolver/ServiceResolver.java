package kr.co.koreanmagic.web2.support.argresolver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.service.GenericService;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

/*
 * Service 객체 찾아주기
 * 1) web.support.Service 인터페이스의 getServiceClass와
 * 2) RequestInfo를 활용한다.
 */
@Component
public class ServiceResolver implements HandlerMethodArgumentResolver {

	Logger logger = Logger.getLogger(getClass());
	Map<String, GenericService<?, ? extends Serializable>> serviceMap;
	
	
	@Autowired
	public void service(List<GenericService<?, ? extends Serializable>> serviceList) {
		serviceMap = new HashMap<>();
		for(GenericService<?, ? extends Serializable> service : serviceList) {
			serviceMap.put( StringUtils.uncapitalize(service.getServiceClass().getSimpleName()), service );
		}
	}
	
	/*
	 * @Service가 붙어있어야 한다.
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Service.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		Service anno_service = parameter.getParameterAnnotation(Service.class);
		HttpServletRequest req = (HttpServletRequest)webRequest.getNativeRequest();
		String searchName = null;
		
		// 쿼리로 찾으면
		if( (searchName = anno_service.param()).length() != 0 )
			return get( req.getParameter(searchName) );
		
		// 패스로 찾으면
		if( (searchName = anno_service.path()).length() != 0 ) {
			Map<String,String> pathVariables = (Map<String, String>)req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			return get( pathVariables.get(searchName) );
		}
		
		// Annotation 파라미터 검사  Method ParameterType
		if( (searchName = anno_service.value()).length() != 0 )
			return get(searchName);
		
		return get( serviceName(parameter.getParameterType()) ); 
		
	}
	
	// 우선순위 ==   @Service.entity > ParameterType > servletPath 
	public Object get(String value) {
		Object result = serviceMap.get(value);
		
		if(result == null)
			throw new RuntimeException(value + "  에 해당하는 service 객체가 없습니다.");
		
		return result;
	}
	// 우선순위 ==   @Service.entity > ParameterType > servletPath 
	public Object searchService(List<String> nameList) {
		Object result = null;
		
		for(String name : nameList) {
			if( (result = serviceMap.get(name)) != null )
				break;
		}
		
		if(result == null)
			throw new RuntimeException(String.join("/", nameList) + "  에 해당하는 service 객체가 없습니다.");
		
		return result;
	}
	
	private String serviceName( Class<?> type ) {
		String name = type.getSimpleName();
		return StringUtils.uncapitalize( name.substring(0, name.indexOf("Service")) );
	}
}

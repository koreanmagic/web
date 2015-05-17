package kr.co.koreanmagic.web2.support.argresolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.NativeWebRequest;

public class Utils {
	
	public static HttpServletRequest cast(NativeWebRequest webRequest) {
		return (HttpServletRequest)webRequest.getNativeRequest();
	}

}

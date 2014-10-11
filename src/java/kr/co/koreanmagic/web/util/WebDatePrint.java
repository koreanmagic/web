package kr.co.koreanmagic.web.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class WebDatePrint {
	
	
	// HttpServletRequest 어트리뷰트 목록
	public static List<String> printRequestAttribute(HttpServletRequest request) {
		return printRequestAttribute(request, " : ");
	}
	public static List<String> printRequestAttribute(final HttpServletRequest request, final String seperator) {
		Enumeration<String> names = request.getAttributeNames();
		return createList(names,
				new GetDate() {
					@Override public Object get(String key) {
						return request.getAttribute(key);
					}
					@Override public String getSeperator() {
						return seperator;
					}
		});
	}
	
	
	// HttpServletRequest 헤더 목록
	public static List<String> printRequestHeader(HttpServletRequest request) {
		return printRequestAttribute(request, " : ");
	}

	public static List<String> printRequestHeader(
			final HttpServletRequest request, final String seperator) {
		Enumeration<String> names = request.getHeaderNames();
		return createList(names, new GetDate() {
			@Override
			public Object get(String key) {
				return request.getHeader(key);
			}
			@Override
			public String getSeperator() {
				return seperator;
			}
		});
	}
	
	
	// 실제 로직
	private static List<String> createList(Enumeration<String> e, GetDate getter) {
		List<String> result = new ArrayList<>();
		String name = null;
		while(e.hasMoreElements()) {
			name = e.nextElement();
			result.add(name + getter.getSeperator() + getter.get(name));
		}
		return result;
	}
	
	
	
	private static interface GetDate {
		Object get(String key);
		String getSeperator();
	}

}

package kr.co.koreanmagic.web.controller.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/*
 * 웹자원을 나눠주는 클래스.
 * 캐시기능도 한다.
 */
public abstract class AbstractResourceProvider<T> implements ResourceProvider<T> {
	
	@Autowired private Environment e;
	
	// 환경설정에서 프로퍼티 빼내기
	public String getProperty(String key) {
		return e.getProperty(key);
	}
	
	protected Environment getEnvironment() {
		return this.e;
	}
	
}

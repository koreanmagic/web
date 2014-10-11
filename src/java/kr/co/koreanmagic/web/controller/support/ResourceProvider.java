package kr.co.koreanmagic.web.controller.support;


/*
 * 각 도메인별 리소스 정보를 제공해주는 인터페이스
 * WebResourceProvider에 사용한다.
 */
public interface ResourceProvider<T> {
	
	Class<T> getType();

}

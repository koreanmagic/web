package kr.co.koreanmagic.web.support.page;

/*
 * 페이지의 정보를 담고 있는 객체
 * 페이지의 servletPath 정보를 가지고 있는다.
 */
public interface MenuContext {

	// 현재 페이지 이름을 반환한다. 대부분 Path명과 비슷하게 설정한다.
	@Override String toString();

}


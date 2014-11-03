package kr.co.koreanmagic.web.support.nav;

import java.util.List;

import kr.co.koreanmagic.web.support.page.MenuContext;

/*
 * 메뉴 트리
 * 다르게 생각하면 PageContext의 관계도를 도식화한 객체라고 볼 수 있다.
 */
public interface MenuNavigator {
	
	<T extends MenuNavigator> T getParent();	// 상위 컨텍스트
	MenuContext pageContext();				// 메뉴명
	
	boolean hasChilds();					// 하위 메뉴가 있는지?
	List<? extends MenuNavigator> childs();		// 하위 네비게이터 반환
	

	String getPath();						// 부모까지 모두 선택해서 반환
	boolean isCurrent();					// 현재 페이지인지 	확인
	
	
}

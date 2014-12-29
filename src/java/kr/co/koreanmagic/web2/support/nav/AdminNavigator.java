package kr.co.koreanmagic.web2.support.nav;

import kr.co.koreanmagic.web.support.nav.GeneralNavigator;

/*
 * 현재페이지 기능을 넣기 위한 Wrapper 객체
 */
public class AdminNavigator extends GeneralNavigator {
	
	public AdminNavigator(String path) {
		super(new DefaultMenuContext(path));
	}
	
	/*
	 * 메서드 체인을 위한 메서드
	 * setName을 통해 MenuContext의 name을 설정하기 위한 루틴
	 * setName은 GenericMenuContext에 구현되어 있다.
	 * 
	 * 위 생성자에서 생성했던 DefaultMenuContext으로 캐스팅해서 setName 메서드를 사용한다. 
	 */
	public AdminNavigator setName(String name) {
		((DefaultMenuContext)getContext()).setName(name);
		return this;
	}
	
	
}

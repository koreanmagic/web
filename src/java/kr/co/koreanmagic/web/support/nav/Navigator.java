package kr.co.koreanmagic.web.support.nav;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

/*
 * 메뉴 트리
 * 다르게 생각하면 PageContext의 관계도를 도식화한 객체라고 볼 수 있다.
 * 
 * 2014-11-05:: pm 11:04
 * Iterator에 대해.
 * Iterator는 아마 자바 세계에서는 일회용 객체로 취급하는 것 같다.
 * 프리마커를 사용하면서 알게 된 것인데, 프리마커에서는 Iterator를 단순히 루프 용도로만 취급해버린다.
 * Iterator 객체에 루프 이외의 용도, 즉 빈 메서드나 다른 루틴 메서드를 요청할 경우 에러가 떠버린다.
 * 일반 객체 자체에 Iterator 구현해버리면, 프리마커에서는 해당 객체를 도저히 사용할 수가 없다.
 * 
 */
public interface Navigator extends Cloneable, Iterable<Navigator> {
	
	<T extends Navigator> T getParent();	// 상위 컨텍스트
	MenuContext getContext();
	
	/*
	 * 
	 * 1) Navigator는 반드시 reflesh로 초기화를 해주어야 한다.
	 * 초기화에 대한 강제력은 없다.
	 * Navigator 구현객체는 아마도 세션에 의해 공유될 것인데, 계속해서 사용되는 객체에 초기화에 대한 기준을 세우기 어렵기 때문이다.
	 * 따라서 reflesh 메서드는 반드시 어플리케이션 구동 프로세서에 밀접히 연관되어 동작되는 걸 강력히 권장한다. 
	 * reflesh에 의해 전달된 Request 객체는 MenuContext의 load까지 내려간다. 
	 * 
	 * NoAccessMenuException::
	 * 모든 MenuContext은 Request를 분석하여, 해당 메뉴의 접속 여부를 결정한다.
	 * 만약 유효하지 않은 접근이라면 NoAccessMenuException를 발생시킨다.
	 * 
	 * 대부분의 경우 애초에 접근할 수 없도록, ViewPage에서 나타나지도 않겠지만,
	 * 악의적인 사용자가 URL주소를 직접 입력한 경우에 유용하게 쓰일 수 있다. 
	 */
	void reflesh(HttpServletRequest req) throws NoAccessMenuException;
	
	boolean isOn();
	boolean isHidden();
	String getPath();						// 부모까지 모두 선택해서 반환
	
	
}

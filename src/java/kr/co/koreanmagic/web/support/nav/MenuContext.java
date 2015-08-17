package kr.co.koreanmagic.web.support.nav;

import javax.servlet.http.HttpServletRequest;

/*
 * 메뉴의 정보(주소)를 담고 있는 객체
 * 
 */
public interface MenuContext {

	
	/*
	 * 사용자에게 보여질 메뉴의 이름
	 * <a href=''> $VALUE$ </a>
	 */
	String getName();
	
	/*
	 * ViewPage 상에서 href=''에 들어갈 값
	 */
	String getUrl();
	
	
	/*
	 * 매 접속때마다 MenuContext를 갱신한다.
	 * Session에 따라 다른 값을 가져야 하거나, 메뉴 자체를 감출수도 있다.
	 * navigatorPath:: 현재 menuContext가 속한 Navigator 객체의 getPath() 값이다.
	 * 
	 * ★★★ load()를 통해서 Navigator의 isHidden 반환 값을 결정해야 한다.
	 * false를 반환하면 네비게이터 상에 나타나지 않는다. 
	 * boolean의 의미를 load 성공/실패로 생각하면 편하다
	 */
	boolean load(HttpServletRequest req, String navigatorPath);
	
	
	/*
	 * 반드시 오버라이딩 해야 한다.
	 * 이 값은 실제 요청 URL값에 매핑되는 값이며, Navigator상에서의 위치값이다. 
	 */
	@Override String toString();

}

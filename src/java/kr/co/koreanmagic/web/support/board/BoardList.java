package kr.co.koreanmagic.web.support.board;

import java.util.List;
import java.util.Map;


/*
 * 페이징객체는 오직 페이지 넘버만 관리한다.
 * URL을 재작성하거나 리스트를 담당하는건 Board 인터페이스의 몫이다.
 * 사실 주로 사용하게 될 객체는 Board구현 객체다.
 * Paging은 오직 페이징 계산에만 전념하기 위해 최소한의 로직만 담았다.
 */
public interface BoardList<T> {
	
	int getRowCount();	// 총 레코드 갯수
	
	int getLinks();		// 버튼 갯수
	int getSize();			// 리스트 갯수
	
	int getThis(); 		// 현재페이지 넘버
	int getLength(); 		// 리스트사이즈로 나누어진 계산된 총 페이지 수
	
	// 건너뛰기 넘버
	int getPrev();
	int getNext();
	
	// 페이지 넘버 관련
	int[] getBefore(); 	// 현재 페이지 이전 페이지 갯수
	int[] getAfter(); 	// 현재 페이지 다음 페이지 갯수
	
	void addQuery(String key, Object value); // 단순 페이징 쿼리외에 추가적으로 붙일 쿼리
	void addQuery(Map<String, String[]> map); // req.getParameterMap()이 반환하는 맵은 Map<String, String[]> 이다.
	
	List<T> getList();				// 게시판 목록
	
	
	String getQuery();					// 기본값 쿼리
	public String getQuery				// 각 요소를 변경시켜서 내보낸다.
				(Map<String, String> replaceMap);
	
	
	String getPath();						//	servletPath
}

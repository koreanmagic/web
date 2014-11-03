package kr.co.koreanmagic.web.support.board;

import java.util.List;

/*
 * 게시판을 위한 빈 객체
 * 주로 스프링 MVC 자동바인딩에 사용할 것이므로 생성자는 기본 생성자만 쓰고 setter 메소드를 사용한다.
 * 1) 리스트 갯수
 * 2) 
 */


public interface BoardList<T> {
	
	int getRowSize(); // 리스트 행 수
	int getTotalRecord(); // 총 레코드
	
	List<T> getList(); // 리스트
	

}

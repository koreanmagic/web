package kr.co.koreanmagic.web.controller.support;

import java.util.List;

/*
 * 게시판을 위한 빈 객체
 * 주로 스프링 MVC 자동바인딩에 사용할 것이므로 생성자는 기본 생성자만 쓰고 setter 메소드를 사용한다.
 * 1) 리스트 갯수
 * 2) 
 */


public interface BoardList<T> {
	
	int getTotalPageNum(); // 총 페이지 수
	
	int getSize(); // 리스트 갯수
	int getRemain(); // 남아있는 페이지 갯수
	int getPage(); // 현재페이지 넘버
	int getTotalRecord(); // 총 레코드
	
	int getStartNum(); // 넘버링 시작번호
	
	String getPath(); // 게시판 servletPath
	String getQueryString();
	
	List<T> getList(); // 리스트
	
	BoardType getType(); // 게시판 타입. 확장포인트
	
	//페이지 넘버 관련
	int[] getPrevPageCount(); // 이전 페이지 갯수
	int[] getNextPageCount(); // 다음 페이지 갯수
	
	int getPrevNum(); // 이전 페이지가 있는지?
	int getNextNum(); // 다음 페이지가 있는지?
	
	

}

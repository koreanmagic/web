package kr.co.koreanmagic.web.controller.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.support.WorkBoardList;

import org.apache.log4j.Logger;
import org.junit.Test;

public class GenericBoardList_TEST {

	Logger logger = Logger.getLogger("test");
	
	//@Test
	public void 남은페이지수계산() {
		assertThat(getBoardList(20, 101, 5).getRemain(), is(1));
	}
	
	@Test(expected=RuntimeException.class)
	public void 에러테스트() {
		GenericBoardList<Work> boardList = new WorkBoardList();
		// 자동바인딩으로 채워지는 정보  page=3&list=20
		boardList.setSize(20); // 리스트 수
		boardList.setPage(4); // 현재 페이지 (총페이지에서의 포지션
		assertThat("총 레코드수가 설정되지 않았다는 런타임 에러", printPage(boardList), is("1 2 3 [4] 5 6"));
	}
	
	private void assertRemain(GenericBoardList<?> boardList, int prev, int next) {
		assertThat(boardList.getPrevNum(), is(prev));
		assertThat(boardList.getNextNum(), is(next));
	}
	
	@Test
	public void 페이지넘버계산_1() {
		GenericBoardList<?> boardList = getBoardList(1, 1, 1);
		assertThat(printPage(boardList), is("[1]"));
		assertThat(boardList.getStartNum(), is(1));
		assertRemain(boardList, -1, -1);
	}
	@Test
	public void 페이지넘버계산() {
		GenericBoardList<?> boardList = getBoardList(20, 101, 1);
		assertThat(printPage(boardList), is("[1] 2 3 4 5 6"));
		assertThat(boardList.getStartNum(), is(1));
		assertRemain(boardList, -1, -1);
	}
	@Test
	public void 페이지넘버계산0() {
		GenericBoardList<?> boardList = getBoardList(20, 101, 2);
		assertThat(printPage(boardList), is("1 [2] 3 4 5 6"));
		assertThat(boardList.getStartNum(), is(21));
		assertRemain(boardList, -1, -1);
	}
	
	@Test
	public void 페이지넘버계산2() {
		GenericBoardList<?> boardList = getBoardList(20, 101, 4);
		assertThat(printPage(boardList), is("1 2 3 [4] 5 6"));
		assertThat(boardList.getStartNum(), is(61));
		assertRemain(boardList, -1, -1);
	}
	
	@Test
	public void 페이지넘버계산3() {
		GenericBoardList<?> boardList = getBoardList(20, 101, 6);
		assertThat(printPage(boardList), is("1 2 3 4 5 [6]"));
		assertThat(boardList.getStartNum(), is(101));
		assertRemain(boardList, -1, -1);
	}
	
	@Test
	public void 페이지넘버계산4() {
		GenericBoardList<?> boardList = getBoardList(10, 200, 11);
		assertThat(printPage(boardList), is("[11] 12 13 14 15 16 17 18 19 20"));
		assertThat(boardList.getStartNum(), is(101));
		assertRemain(boardList, 10, -1);
	}
	@Test
	public void 페이지넘버계산5() {
		GenericBoardList<?> boardList = getBoardList(10, 200, 15);
		assertThat(printPage(boardList), is("11 12 13 14 [15] 16 17 18 19 20"));
		assertThat(boardList.getStartNum(), is(141));
		assertRemain(boardList, 10, -1);
	}
	@Test
	public void 페이지넘버계산6() {
		GenericBoardList<?> boardList = getBoardList(10, 300, 15);
		assertThat(printPage(boardList), is("11 12 13 14 [15] 16 17 18 19 20"));
		assertThat(boardList.getStartNum(), is(141));
		assertRemain(boardList, 10, 21);
	}
	
	@Test
	public void 페이지넘버계산7() {
		GenericBoardList<?> boardList = getBoardList(10, 300, 20);
		assertThat(printPage(boardList), is("11 12 13 14 15 16 17 18 19 [20]"));
		assertThat(boardList.getStartNum(), is(191));
		assertRemain(boardList, 10, 21);
	} 
	
	@Test
	public void 페이지넘버계산8() {
		GenericBoardList<?> boardList = getBoardList(10, 260, 21);
		assertThat(printPage(boardList), is("[21] 22 23 24 25 26"));
		assertThat(boardList.getStartNum(), is(201));
		assertRemain(boardList, 20, -1);
	}
	
	@Test
	public void 페이지넘버계산8_값을넘어갔을때() {
		GenericBoardList<?> boardList = getBoardList(10, 260, 30);
		assertThat(printPage(boardList), is("21 22 23 24 25 [26]"));
		assertThat(boardList.getStartNum(), is(251));
		assertRemain(boardList, 20, -1);
	}
	
	
	@Test
	public void 페이지넘버계산_pageNumLen다르게1() {
		GenericBoardList<?> boardList = getBoardList(8, 260, 21);
		boardList.setPageNumLen(8);
		assertThat(printPage(boardList), is("17 18 19 20 [21] 22 23 24"));
		assertThat(boardList.getStartNum(), is(161));
		assertRemain(boardList, 16, 25);
	}
	
	@Test
	public void 페이지넘버계산_pageNumLen다르게2() {
		GenericBoardList<?> boardList = getBoardList(8, 260, 1);
		boardList.setPageNumLen(8);
		assertThat(printPage(boardList), is("[1] 2 3 4 5 6 7 8"));
		assertThat(boardList.getStartNum(), is(1));
		assertRemain(boardList, -1, 9);
	}
	
	@Test
	public void 페이지넘버계산_pageNumLen다르게3() {
		GenericBoardList<?> boardList = getBoardList(8, 240, 30);
		boardList.setPageNumLen(8);
		assertThat(printPage(boardList), is("25 26 27 28 29 [30]"));
		assertThat(boardList.getStartNum(), is(233));
		assertRemain(boardList, 24, -1);
	}
	
	
	@Test
	public void 페이지넘버계산_값바꿨을때_결과값변환확인() {
		assertThat(printPage(getBoardList(20, 5, 1)), is("[1]"));
		assertThat(printPage(getBoardList(20, 100, 1)), is("[1] 2 3 4 5"));
	}
	
	
	
	// 게시판 만들기
	private GenericBoardList<?> getBoardList(int listSize, int totalRecordNum, int currentPageNum) {
		return getBoardList(listSize, totalRecordNum, currentPageNum, 10);
	}
	private GenericBoardList<?> getBoardList(int listSize, int totalRecordNum, int currentPageNum, int pageLen) {
		GenericBoardList<?> boardList = new WorkBoardList();
		boardList.setSize(listSize); // 게시판 행 수
		boardList.setTotalRecordNum(totalRecordNum); // 총 레코드 수
		boardList.setPage(currentPageNum); // 현재 페이지
		boardList.setPageNumLen(pageLen); // 페이지 네비게이션 사이즈
		return boardList;
	}
	
	// 페이지 네비게이션 print
	private String printPage(GenericBoardList<?> boardList) {
		String result = "";
		for(int s : boardList.getPrevPageCount())
			result += s + " ";
		result += "[" + boardList.getPage() + "] ";
		for(int e : boardList.getNextPageCount())
			result += e + " ";
		return result.substring(0, result.length()-1);
	}

}

package kr.co.koreanmagic.web.support.board;

public interface BoardPaging {

	//페이지 넘버 관련
	int[] getPrevPage(); // 이전 페이지 갯수
	int getCurrentPage(); // 현재페이지 넘버
	int[] getNextPage(); // 다음 페이지 갯수
	
	int getTotalPageSize(); // 총 페이지 수
	
}

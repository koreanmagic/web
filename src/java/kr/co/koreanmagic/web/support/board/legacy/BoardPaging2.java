package kr.co.koreanmagic.web.support.board.legacy;

public interface BoardPaging2 {

	
	void set(int[] prev, int[] next);
	
	int getLinkSize();
	
	int[] getPrevPage(); // 이전 페이지 갯수
	int getCurrentPage(); // 현재페이지 넘버
	int[] getNextPage(); // 다음 페이지 갯수
	
	int getTotalPage();
	
}

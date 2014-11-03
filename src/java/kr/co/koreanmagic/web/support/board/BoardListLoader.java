package kr.co.koreanmagic.web.support.board;


/*
 * 게시판을 구성하는 데이터를 넘겨주는 BoardList를 구현하면서
 * 동시에 DB에서 게시판 데이터를 가지고 오는 임무를 맡게 한다. 
 */
public interface BoardListLoader<T> extends BoardList<T>{
	
	/*
	 * SQL의 LIMIT 1, 20 을 생각하면 된다.
	 */
	void load(int start, int limit);

}

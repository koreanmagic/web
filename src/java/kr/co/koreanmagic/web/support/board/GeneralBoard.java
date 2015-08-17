package kr.co.koreanmagic.web.support.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


/*
 * 
 * BoardManager를 사용하기 위한 최소정보
 * 1) 생성할 링크 버튼 수
 * 2) 현재 페이지번호
 * 3) 게시판 리스트 갯수
 * 4) 전체 데이터 행 수
 * 5) 절대경로
 * 6) 데이터 정렬기준 
 * 
 * 계산방법 :
 * ① (전체 게시물 수 / 한 화면 표시행수) ==> 총 버튼 수
 * ② 총 버튼 번호에서 현재 페이지를 찾는다.
 * ③ 버튼 수에 맞게 현재페이지 앞뒤 번호를 나열한다.   
 *  
 */
public class GeneralBoard<T> implements BoardList<T>, PagingRequest {

	Logger logger = Logger.getLogger(getClass());
	
	private int rowCount;	// 전체 행수
	private int length;		// (전체 게시물 수 / 한 화면 표시행수)
	
	private String order;		// ORDER BY
	
	private int links;			// 버튼 갯수
	private int size;				// 리스트 행 수

	private int[] before = new int[0];		// 이전 번호 나열
	private int currentPage;					// 현재 페이지
	private int[] after = new int[0];		// 이후 번호 나열
	
	private int prevPage = -1;		// << 페이지 번호
	private int nextPage = -1;		// >> 페이지 번호
	
	private String addQuery = "";	// 추가할 쿼리
	private String path;			// 요청받은 URL
	private Map<String, String> queryMap = new HashMap<>();
	
	private List<T> list;		// 게시물 리스트
	
	
	// 링크버튼 수, 한 화면 표시 갯수, 현재페이지는 바꿀 수 없다. 
	public GeneralBoard(int links, int size, int currentPage, String order, String path) {
		this.links = links;
		this.size = size;
		this.currentPage = currentPage;
		this.order = order;
		this.path = path;
		
		queryMap.put( "size", String.valueOf(size) );
		queryMap.put( "page", String.valueOf(currentPage) );
		queryMap.put( "order", order );
		
	}
	
	
	// 전체 컬럼수 입력
	// ★★★ 컬럼수를 입력하면서 계산이 된다. ★★★
	// 0이 들어올수도 있다.
	public GeneralBoard<T> setRowCount(int rowCount) {
		this.rowCount = rowCount;
		setLength(rowCount);		// 페이지 계산
		if(rowCount > 0) compute();
		return this;
	}
	@Override
	public int getRowCount() {
		return this.rowCount;
	}
	
	// 한 화면에 보여질 갯수
	public int getSize() {
		return this.size;
	}
	
	
	// 링크 버튼 수
	@Override
	public int getLinks() {
		return (links > length) ?  length : links; 
	}
	 
	
	// 계산된 전체 페이지
	private void setLength(int rowCount) {
		this.length = rowCount / size; 
		this.length = (rowCount % size) > 0 ? this.length + 1 : this.length;
	}
	@Override
	public int getLength() {
		return this.length;
	}
	
	
	
	@Override
	public int getThis() {
		return this.currentPage;
	}
	
	
	// 현재 페이지 전후로 들어갈 숫자 배열
	@Override
	public int[] getBefore() {
		return this.before;
	}
	@Override
	public int[] getAfter() {
		return this.after;
	}
	
	
	
	// 나열된 페이지 이전, 이후 페이지
	@Override
	public int getPrev() {
		return this.prevPage;
	}
	@Override
	public int getNext() {
		return this.nextPage;
	}	

	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 연산 유틸 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * 현재 페이지 우측으로 나열될, 남은 페이지 연산
	 * :: 전체 페이지에서 현재 페이지를 빼서 뒤로 얼마나 남았는지 계산한다.
	 * [4] 5, 6, 7 >> 
	 */
	private int remain() {
		int remain = getLength() - getThis();
		// remain이 음수라는건 currentPageNum이 적정페이지 범위보다 더 큰 숫자로 들어왔다는 얘기가 된다.
		if(remain < 0 ) {
			currentPage = getLength(); // 이때는 마지막 페이지로 재설정해준다.
			remain = 0;
		}
		return remain;
	}
	
	// 게시판 페이지 네비게이션 계산
	private void compute() {
		int before = 1, after = 0, 				// 현재 번호 앞뒤로 들어갈 숫자 갯수
			links = getLinks(),				// pos는 currentPageNum의 위치
			current = getThis() % links,	// 현재페이지를 버튼갯수로 나눈다. 링크 버튼 중 현재페이지가 어디에 위치해야 하는지
			remain = remain()		// 전체페이지에서 현재 페이지를 뺀 나머지. 즉 남은 페이지
			; 
		
		if(current == 0) { // [#CASE 1] 딱 떨어지면 현재 페이지가 맨 뒤
			before = links - 1;
		}
		
		else  {
			
			if (current == 1) { // [#CASE 2] 나머지가 1이면 맨 앞자리
				before = 0;
				after = links - 1; // 뒷자리 숫자는 하나를 제외한 나머지
			}
			
			else { // [#CASE 3] 나머지가 있으면 앞뒤로 숫자가 있을 수 있다. 
				before = current - 1;
				after = links - current; // 버튼 갯수에서 현재 페이지 
			}
			
			// 현재페이지 뒤로 들어갈 페이지가 remain보다 크면 remain 갯수로 맞추어준다.
			after = (after > remain) ? remain : after;
		}
		
		this.before = fill0(setPrev(currentPage - before), before);
		this.after = fill0((currentPage + 1), after);
		
		// 현재페이지번호와 남은 페이지를 더하면 버튼으로 표시할 수 있는 가장 마지막 숫자가 된다.
		// 이 숫자와 전체 페이지 갯수를 확인해서 페이지가 더 남아있는지 설정한다.
		setNext(currentPage + after);
	}
	// 이전페이지 계산
	private int setPrev(int start) {
		if(start > 1) {
			this.prevPage = start - 1;
		}
		return start;
	}
	// 다음 페이지 계산
	private void setNext(int end) {
		if(getLength() > end) {
			this.nextPage = end + 1;
		}
	}
	
	
	//시작 넘버를 갯수만큼 배열에 채워준다.
	private int[] fill0(int start, int num) {
		int[] buf = new int[num];
		for(int i = 0; i < num; i++) {
			buf[i] = start++;
		}
		return buf;
	}
	
	
	/* ****************  Board 구현  **************** */
	// 게시물 
	public GeneralBoard<T> setList(List<T> list) {
		this.list = list;
		return this;
	}
	@Override
	public List<T> getList() {
		if(this.list == null) return Collections.EMPTY_LIST;
		return this.list;
	}
	
	
	// 요청 URL
	public GeneralBoard<T> setPath(String path) {
		this.path = path;
		return this;
	}
	@Override
	public String getPath() {
		return this.path;
	}	
	

	// 추가적으로 붙일 쿼리
	@Override
	public void addQuery(String key, Object value) {
		queryMap.put(key, value.toString());
	}
	@Override
	public void addQuery(Map<String, String[]> map) {
		for(Entry<String, String[]> m : map.entrySet()) {
			if(!m.getKey().matches("size|page|order")) {
				queryMap.put(m.getKey(), m.getValue()[0]);
			}
		}
		
	}

	// 페이지번호만 바꿔서 쿼리를 내보낸다.
	@Override
	public String getQuery() {
		return getQuery(Collections.EMPTY_MAP);
	}
	@Override
	public String getQuery(Map<String, String> m) {
		
		// 프리마커에서 리터럴로 만들어주는 Map은 불변객체인 것 같다. remove메서드를 호출하면 런타임 에러를 던진다.
		Map<String, String> replaceMap = new HashMap<>(m);
		
		StringBuilder builer = new StringBuilder();
		
		String key = null,
				value = null;
		
		for(Entry<String, String> entry : queryMap.entrySet()) {
			
			key = entry.getKey();
			value = replaceMap.get(key);
			
			
			if(value != null) {
				replaceMap.remove(key);
				if(value.length() > 0)	// 공백문자가 들어오면 query에서 삭제한다.
					builer.append("&").append(key).append("=").append( value  );
			}
			else builer.append("&").append(key).append("=").append( entry.getValue() );
			
		}
		
		if(!replaceMap.isEmpty()) {
			for(Entry<String, String> entry : replaceMap.entrySet()) {
				if(entry.getValue().length() > 0)
					builer.append("&").append(entry.getKey()).append("=").append( entry.getValue() );
			}
		}
		
		return builer.substring(1, builer.length());
	}
	
	
	/* ****************  PagingRequest 구현  **************** */

	@Override
	public int getStart() {
		return (getThis() - 1) * getSize();
	}
	@Override
	public int getLimit() {
		return getSize();
	}
	@Override
	public String getOrder() {
		return this.order;
	}
	
	
}

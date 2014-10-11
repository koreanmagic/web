package kr.co.koreanmagic.web.controller.support;

import java.util.List;

/*
 * 게시판 리스트 관련 객체
 * 총레코드수를 반드시 입력해주어야 한다.
 * 
 * 게시판을 나타내기 위한 각종 정보와
 * 페이지 네비게이션을 제공한다.
 * 
 */


public abstract class GenericBoardList<T> implements BoardList<T> {
	
	private int totalRecord; // 총 레코드 수 <-- 반드시 입력해주어야 한다.
	private List<T> list; // 리스트

	private int total; // 총 페이지
	private int currentPageNum; // 현재 페이지
	private int size; // 보여질 리스트 갯수, 기본값 20개
	private int remain; // 남은 페이지
	private String path; // 페이지 번호에 부여될 URL
	private String query;
	
	private int startNum; // 시작 번호
	private int endNum; // 마지막 번호
	
	
	private boolean update; // 값이 바뀌었는지 확인
	private Class<T> domain;
	
	
	public GenericBoardList(Class<T> type) {
		this.domain = type;
		init();
	}
	
	// 재사용을 위한 초기화
	public void init() {
		setPage(1);
		setSize(5);
		setRemain(0);
		setQueryString("");
		setPath("");
		setList(null);
	}
	
	// 이전페이지가 남아있는지?
	@Override
	public int getPrevNum() {
		if(startNum == 1 || startNum <= pageNumLen) return -1;
		return startNum - 1;
		
	}

	// 다음페이지가 남아있는지?
	@Override
	public int getNextNum() {
		if((endNum - getPage()) >= getRemain()) return -1;
		return endNum + 1;
	}
	
	// 페이지 넘버링 먹였을때 시작번호
	@Override
	public int getStartNum() {
		return (size * (currentPageNum - 1)) + 1;
	}


	// 총 레코드 수를 받으면, 리스트 행 수로 나누어서 총 페이지를 계산한다.
	// 아래 세 값이 새로 설정되면 계산을 다시 해야 한다. update 알람표시 설정
	public void setTotalRecordNum(int totalRecord) {
		this.totalRecord = totalRecord;
		update();
	}
	public void setPage(int page) {
		this.currentPageNum = (page < 1) ? 1 : page; // 페이지번호는 1부터 시작하므로 0이거나 음수면 1로 바꾼다.
		update();
	}
	public void setSize(int size) { 
		this.size = size;
		update();
	}
	
	// 업데이트 확인
	private void update() { this.update = true; }
	
	private void setRemain(int remain) { this.remain = (remain < 0) ? 0 : remain; }
	public void setList(List<T> list) { this.list = list; }
	public void setPath(String path) { this.path = notNull(path); }
	public void setQueryString(String query) { this.query = notNull(query); }
	@Override public String getPath() { return this.path; }
	@Override public String getQueryString() { return this.query; }
	
	private String notNull(String s) {
		return (s == null) ? "" : s;
	}
	
	
	
	
	/* 이 메서드들은 값을 반환하기 전에 서로의 값을 참조한 연산이 필요하다. */
	// 핵심 메서드. 각종 데이터 확인과, 연산을 책임진다.
	private void refresh() {
		if(!update) return;
		throwRuntimeException(totalRecord, 1, "총 레코드 수가 설정되지 않았습니다.");
		remainNum(); // 남은 페이지 계산
		pageNav(); // 게시판 페이지 네비게이션 계산
		this.update = false;
	}
	@Override // ★ 외부 설정이 필요한 값
	public int getPage() {
		refresh();
		return this.currentPageNum;
	}
	@Override // ★ 외부 설정이 필요한 값
	public int getTotalRecord() {
		refresh();
		return this.totalRecord;
	}
	@Override // 계산을 통해 얻는 값
	public int getTotalPageNum() {
		refresh();
		return this.total;
	}
	@Override // 계산을 통해 얻는 값
	public int getRemain() {
		refresh();
		return this.remain;
	}
	@Override // 계산을 통해 얻는 값
	public int[] getPrevPageCount() {
		refresh();
		return this.beforeNums;
	}
	@Override // 계산을 통해 얻는 값
	public int[] getNextPageCount() {
		refresh();
		return this.afterNums;
	}
	
	
	@Override
	public List<T> getList() {
		return this.list;
	}
	@Override
	public int getSize() {
		return this.size;
	}
	@Override
	public BoardType getType() {
		throw new UnsupportedOperationException("하위클래스에서 구현하세요!");
	}
	
	
	
	
	/*
	 *  1 2 3 4 [5] 6 7 8 9
	 *  페이지 넘버 관련
	 */
	private int pageNumLen = 10;
	
	private int[] beforeNums; // 페이지 번호 맨 앞자리 단위
	private int[] afterNums; // 페이지 번호 맨 뒷자리
	
	public void setPageNumLen(int pageNumLen) { this.pageNumLen = pageNumLen; }
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 연산 유틸 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 남은 페이지 계산
	private void remainNum() {
		int remain = 0;
		
		total = totalRecord/getSize(); // 총 레코드수를 게시판 리스트 사이즈로 나눈다. 페이지수 생성
		total = (totalRecord % getSize() != 0) ? total + 1 : total; // 나머지가 있으면 짜투리가 있다는 얘기이므로 페이지 수 1 추가
		// setRemain 남은 페이지
		remain = total - currentPageNum; // remain이 음수라는건 currentPageNum이 적정페이지 범위보다 더 큰 숫자로 들어왔다는 얘기가 된다.
		if(remain < 0 ) setPage(total); // 이때는 마지막 페이지로 재설정해준다.
		setRemain(remain); // setRemain() 메소드가 음수 변환을 해주니까 그냥 넣어준다.
	}
	
	// 게시판 페이지 네비게이션 계산
	private void pageNav() {
		int _beforeNum = 1, _afterNum = 0, // 현재 번호 앞뒤로 들어갈 숫자 갯수
			pos; // pos는 currentPageNum의 위치
		
		pos = currentPageNum % pageNumLen; // 현재페이지를 버튼갯수로 나눈다. 포지션 계산
		if(pos == 0) { // [#CASE 1] 딱 떨어지면 현재 페이지가 맨 뒤
			_beforeNum = pageNumLen - 1;
		} else  {
			if (pos == 1) { // [#CASE 2] 나머지가 1이면 맨 앞자리
				_beforeNum = 0;
				_afterNum = pageNumLen - 1; // 뒷자리 숫자는 하나를 제외한 나머지
			} else { // [#CASE 3] 나머지가 있으면 앞뒤로 숫자가 있을 수 있다.
				_beforeNum = pos - 1;
				_afterNum = pageNumLen - pos; // 버튼 갯수에서 현재 페이지 
			}
			_afterNum = (_afterNum > remain) ? remain : _afterNum;
		}
		
		this.startNum = currentPageNum - _beforeNum;
		this.endNum = currentPageNum + _afterNum;
				
		this.beforeNums = fill0((currentPageNum - _beforeNum), _beforeNum);
		this.afterNums = fill0((currentPageNum + 1), _afterNum);
	}
	
	private int throwRuntimeException(int target, int min, String message) {
		if(target < min) throw new RuntimeException(message);
		return target;
	}

	//시작 넘버를 갯수만큼 배열에 채워준다.
	private int[] fill0(int start, int num) {
		int[] buf = new int[num];
		for(int i = 0; i < num; i++) {
			buf[i] = start++;
		}
		return buf;
	}
	
	@Override
	public String toString() {
		return "[" + domain  + "@" + hashCode()
				+ " ? totalRecord=" + this.totalRecord 
				+ " & currentPage=" + this.currentPageNum
				+ " & listSize=" + getSize()
				+ "]";  
	}
	
}

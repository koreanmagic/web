package kr.co.koreanmagic.web.support.board;


/*
 * 페이징을 담당하는 미구현 클래스
 * set(총 페이지 수, 현재 페이지, 나열할 페이지 버튼 수) 를 입력하면 된다.
 * 
 */
public abstract class BoardPagingImpl implements BoardPaging {

	
	private int totalPageSize;
	private int currentPage;
	private int linkBtnSize;
	private int[] prevPage;
	private int[] nextPage;
	
	protected BoardPagingImpl(int linkSize) {
		setLinkBtnSize(linkSize);
	}
	
	// 총 페이지수, 현재페이지, 나열될 페이지 링크버튼 수
	protected void set(int totalPageSize, int currentPage) {
		this.totalPageSize = totalPageSize;
		this.currentPage =currentPage;
		pageNav();
	}
	
	
	public int getLinkBtnSize() {
		return linkBtnSize;
	}
	public void setLinkBtnSize(int linkBtnSize) {
		this.linkBtnSize = linkBtnSize;
	}
	
	
	@Override
	public int getTotalPageSize() {
		return this.totalPageSize;
	}
	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 연산 유틸 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	private int remain() {
		int remain = getTotalPageSize() - getCurrentPage(); // remain이 음수라는건 currentPageNum이 적정페이지 범위보다 더 큰 숫자로 들어왔다는 얘기가 된다.
		if(remain < 0 ) {
			currentPage = getTotalPageSize(); // 이때는 마지막 페이지로 재설정해준다.
			remain = 0;
		}
		return remain;
	}
	
	// 게시판 페이지 네비게이션 계산
	private void pageNav() {
		int beforePage = 1, afterPage = 0, 			// 현재 번호 앞뒤로 들어갈 숫자 갯수
			pageNumLen = getLinkBtnSize(),			// pos는 currentPageNum의 위치
			currentPos = getCurrentPage() % pageNumLen,	// 현재페이지를 버튼갯수로 나눈다. 포지션 계산
			remain = remain()
			; 
		
		if(currentPos == 0) { // [#CASE 1] 딱 떨어지면 현재 페이지가 맨 뒤
			beforePage = pageNumLen - 1;
		}
		
		else  {
			
			if (currentPos == 1) { // [#CASE 2] 나머지가 1이면 맨 앞자리
				beforePage = 0;
				afterPage = pageNumLen - 1; // 뒷자리 숫자는 하나를 제외한 나머지
			}
			
			else { // [#CASE 3] 나머지가 있으면 앞뒤로 숫자가 있을 수 있다.
				beforePage = currentPos - 1;
				afterPage = pageNumLen - currentPos; // 버튼 갯수에서 현재 페이지 
			}
			
			afterPage = (afterPage > remain) ? remain : afterPage;
		}
		
		this.prevPage = fill0((currentPage - beforePage), beforePage);
		this.nextPage = fill0((currentPage + 1), afterPage);
	}
	
	
	@Override
	public int[] getPrevPage() {
		return this.prevPage;
	}

	@Override
	public int[] getNextPage() {
		return this.nextPage;
	}
	
	//시작 넘버를 갯수만큼 배열에 채워준다.
	private int[] fill0(int start, int num) {
		int[] buf = new int[num];
		for(int i = 0; i < num; i++) {
			buf[i] = start++;
		}
		return buf;
	}

}

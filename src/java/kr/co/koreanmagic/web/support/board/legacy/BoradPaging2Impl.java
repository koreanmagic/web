package kr.co.koreanmagic.web.support.board.legacy;


/*
 * 페이징을 담당하는 클래스
 * set(총 페이지 수, 현재 페이지, 나열할 페이지 버튼 수) 를 입력하면
 * 
 */
public class BoradPaging2Impl implements BoardPaging2 {

	
	private int totalPage;
	private int currentPage;
	private int linkSize;
	private int[] prevPage;
	private int[] nextPage;
	
	BoradPaging2Impl(int linkSize) {
		setLinkSize(linkSize);
	}
	
	// 총 페이지수, 현재페이지, 나열될 페이지 링크버튼 수
	public void set(int[] prev, int[] next) {
		this.prevPage = prev;
		this.nextPage = next;
	}
	
	
	public int getLinkSize() {
		return linkSize;
	}
	public void setLinkSize(int linkSize) {
		this.linkSize = linkSize;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	@Override
	public int getTotalPage() {
		return this.totalPage;
	}
	
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	
	@Override
	public int[] getPrevPage() {
		return this.prevPage;
	}

	@Override
	public int[] getNextPage() {
		return this.nextPage;
	}


}

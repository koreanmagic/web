package kr.co.koreanmagic.web.support.board;

import java.util.List;

/*
 * 게시판 리스트를 사용하기 위해서는
 * 특별한 요건이 있지 않은 이상, 일반적으로 이 클래스를 상속해서 사용하면 된다.
 * 
 * 사용법은 간단한다.
 * init() :: DB에 접속하기전이나, 혹은 아래 두 메서드를 호출하기 전에 선행해야할 작업이 있다면 해준다.
 * load(int, int) :: DB에서 직접 리스트를 가지고 온다. <LIMIT start, limit>
 * rowCount() :: 해당 엔터티(혹은 객체리스트)의 총 갯수를 반환해주면 된다. <COUNT(*)>
 * 
 * 위 두가지 메서드만 구현하면, 페이징 숫자를 알아서 계산해준다.
 * 
 * ★★★★ 재활용이 가능한 객체!! 하지만 스레드 안전하지 않다!!
 * 
 */
public abstract class AbstractBoardListLoader<T> extends BoardPagingImpl implements BoardListLoader<T> {

	
	private int rowSize;
	private int totalRecord;
	private List<T> list;
	
	protected AbstractBoardListLoader() {
		super(10);
	}
	protected AbstractBoardListLoader(int linkSize) {
		super(linkSize);
	}

	
	@Override
	public int getRowSize() {
		return this.rowSize;
	}

	void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	@Override
	public int getTotalRecord() {
		return this.totalRecord;
	}

	@Override
	public List<T> getList() {
		return this.list;
	}

	@Override
	public final void load(int start, int limit) {

		init();
		
		this.list = loadList(start, limit);
		
		/* BoardPaging 구현 */
		set((int)rowCount(), start);
		
		
	}
	
	protected abstract void init();
	protected abstract List<T> loadList(int start, int limit);
	protected abstract long rowCount();
	
}

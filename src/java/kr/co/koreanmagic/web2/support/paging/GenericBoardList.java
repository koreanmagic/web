package kr.co.koreanmagic.web2.support.paging;

import java.util.List;

import kr.co.koreanmagic.web.support.board.GeneralBoardManager;
import kr.co.koreanmagic.web2.servlet.exception.NoSearchListException;

import org.apache.log4j.Logger;

public class GenericBoardList extends GeneralBoardManager {
	Logger logger = Logger.getLogger(getClass());
	
	private StringBuffer buf;
	
	private List<?> list;
	
	public GenericBoardList() {
		this(30);
	}
	
	public GenericBoardList(int linkSize) {
		super(linkSize);
	}
	
	// 가장 마지막에 호출될 메서드, 전체 행수를 입력해준다.
	@Override
	public GenericBoardList setTotalSize(int totalSize) {
		
		if(totalSize < 1) throw new NoSearchListException("");
		
		super.setTotalSize(totalSize);
		super.compute();				// 계산
		
		buf = new StringBuffer("<div class=\"btn-group\">");
		
		if(hasPrev()) tagA(getPrev(), "<<");
		
		for(int i : getBefore()) tagA(i);
		buf.append("<span>").append(getCurrent()).append("</span>");
		for(int i : getAfter()) tagA(i);
		
		if(hasNext()) tagA(getNext(), ">>");
		
		buf.append("</div>");
		
		return this;
	}
	
	public String getPageTag() {
		return this.buf.toString();
	}
	
	private StringBuffer tagA(Object pageNum) {
		return tagA(pageNum, pageNum);
	}
	private StringBuffer tagA(Object pageNum, Object text) {
		return buf.append("<a href=\"")
					.append(getPath())
					.append("?page=")
					.append(pageNum)
					.append("&size=")
					.append(getSize())
					.append("&order=")
					.append(getOrder())
					.append("\">")
					.append(text)
					.append("</a>");
	}

	
	public void setList(List<?> list) { this.list = list; }
	@Override
	public List<?> getList() {
		return this.list;
	}

	@Override
	public String toString() {
		return String.format("[현재페이지: %d, 리스트 갯수: %d, 버튼갯수: %d, 정렬기준: %s, 경로: %s]",
								getCurrent(), getSize(), getLength(), getOrder(), getPath());
	}

	// 페이지는 0부터 시작한다., 1페이지 이후부터는 시작번호에 1을 더한다.
	@Override
	public int getStart() {
		return (getCurrent() - 1) * getSize();
	}

	@Override
	public int getLimit() {
		return getSize();
	}


}

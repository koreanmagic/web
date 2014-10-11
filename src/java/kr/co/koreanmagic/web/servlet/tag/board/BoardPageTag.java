package kr.co.koreanmagic.web.servlet.tag.board;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import kr.co.koreanmagic.web.servlet.tag.GeneralTagSupport;

/*
 * 게시판의 페이지
 */
public class BoardPageTag extends GeneralTagSupport {
	
	/* 앞 뒤를 구분해주는 sign */
	final static int PREV_BTN = 0;
	final static int PREV = 1;
	final static int CURRENT = 2;
	final static int NEXT = 3;
	final static int NEXT_BTN = 4;
	
	private int currentPos;
	
	private int leftPage; // 남은 페이지
	private int numLength = 10; // 나타낼 버튼 갯수
	private int currentPage = 1; // 현재페이지
	private int totalPage;
	

	public void setCurrentPage(int currentPage) {
		this.currentPage = (currentPage <= 0) ? 0 : currentPage;
	}
	
	public void setLeftPage(int leftPage) {
		this.leftPage = (leftPage <= 0) ? 0 : leftPage;
	}
	
	public void setNumLength(int numLength) {
		this.numLength = numLength;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	
	@Override
	public void doTag() throws JspException, IOException {
		/*
		 * 진짜 존나 하루 반나절동안 개고생해서 짠 코드..
		 * TDD의 중요성을 다시 한번 절감하다. 항상 상기하자!! TDD 필수!!
		 */
		int length, // 총 생성될 페이지 버튼 수
			backNum, // 현재 페이지 번호 뒤로 형성될 번호 갯수
			currentPos, // 현재 페이지 번호
			startNum; // 나열될 페이지 번호 맨 앞자리 
		
		currentPos = (currentPage % numLength == 0) ? // 현재 페이지를 버튼 갯수로 나눈 나머지. 현재 페이지 앞쪽에 몇개의 숫자가 있어야 할 것인지 알아냄.
					numLength : // 만약 나머지가 0이라면 현재 페이지가 맨 앞에 있어야 한다는 뜻이다. 버튼 뒤로 버튼 갯수만큼 만든다.
					currentPage % numLength; // 기준 페이지 앞의 갯수
		
		startNum = currentPage - currentPos; // 페이지 먹일 시작 넘버 구함
		backNum = numLength - currentPos; // 남은 자릿수
		
		backNum = (leftPage < backNum) ? leftPage : backNum; // 남은 자릿수 채움
		length = currentPos + backNum; // 합산해서 총 버튼 갯수 구함
		
		JspFragment body = getJspBody();
		
		
		int p = 0, l = 0;
		
		// 앞쪽 버튼
		if(startNum > 0) { // 시작하는 
			setPos(PREV_BTN);
			setAttribute("num", startNum); // pageNum은 시작 페이지번호보다 1이 적은 숫자다.
			body.invoke(null);
		}
		for(int i = 0; i < length; i++) {
			l = i + 1;
			p = startNum + l;
			
			setAttribute("num", p); // ${num}
			
			if(l < currentPos) { // 현재페이지 전
				setPos(PREV);
			} else if(l == currentPos) { // 현재페이지
				setPos(CURRENT);
			} else { // 현재 페이지 다음
				setPos(NEXT);
			}
			body.invoke(null);
		}
		// 앞쪽 버튼
		if(totalPage > p) {
			setPos(NEXT_BTN);
			setAttribute("num", p+1); // 다음페이지 번호를 넘겨준다.
			body.invoke(null);
		}
	}
	
	int getPos() {
		return this.currentPos;
	}
	void setPos(int currentPos) {
		this.currentPos = currentPos; 
	}

}

package kr.co.koreanmagic.web.support.board.legacy;



public class PagingUtil {
	
	
	/*
	 * 2014-10-21 pm9:10 구현문제점
	 * 총 페이지수보다 현재 페이지가 큰 숫자가 들어올 경우.
	 * 현재 페이지를 총페이지 수로 바꿔야 하는데,
	 * BoardPaging2 구현에서 현재페이지를 수정하는 메서드가 없기때문에 수정이 안된다.
	 * 예컨데) 1 2 3 4 [10]  과 같은 결과물이 나올 수 있다는 얘기다.
	 */
	public static BoardPaging2 computePaging(BoardPaging2 p) {
		
		int totalPage = p.getTotalPage(),
			currentPage = p.getCurrentPage(),
			pageNumLen = p.getLinkSize(),
			
			beforePage = 1, afterPage = 0,
			currentPos	= currentPage % pageNumLen,
			remain = totalPage - currentPage
			;
		
		
		if(remain < 0 ) {
			currentPage = totalPage; // 이때는 마지막 페이지로 재설정해준다.
			remain = 0;
		}
		
		/*
		 * 뒷 페이지 계산
		 */
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
		
		p.set(fill0((currentPage - beforePage), beforePage), fill0((currentPage + 1), afterPage));
		
		return p;
	}
	
	//시작 넘버를 갯수만큼 배열에 채워준다.
	private static int[] fill0(int start, int num) {
		int[] buf = new int[num];
		for(int i = 0; i < num; i++) {
			buf[i] = start++;
		}
		return buf;
	}

}

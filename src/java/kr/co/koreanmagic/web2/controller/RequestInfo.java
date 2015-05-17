package kr.co.koreanmagic.web2.controller;

/*
 * 매 요청때마다 들어가는 정보
 * 
 * /{그룹}/{콘트롤러}/{view}/{데이터타입/모델}/{쿼리}
 * ex) /admin/partner/list/customer/333
 */
public interface RequestInfo {
	
	static final String REQUEST_KEY = "requestInfo";
	
	// 그룹네임
	String getGroup();
	// 페이지 이름
	String getCommand();
	String getView();
	String getModel();					// 도메일 모델
	String getQuery();
	
}


package kr.co.koreanmagic.web2.support.nav;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web.support.nav.GenericMenuContext;

/*
 * 각 페이지별 컨텍스트 메뉴
 */
public class DefaultMenuContext extends GenericMenuContext {

	public DefaultMenuContext(String path) {
		super(path);
	}

	private String path; 
	
	@Override
	public String getUrl() {
		return this.path;
	}

	@Override
	public boolean load(HttpServletRequest req, String navigatorPath) {
		this.path = navigatorPath;
		return true;
	}


}

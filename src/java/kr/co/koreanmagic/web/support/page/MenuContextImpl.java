package kr.co.koreanmagic.web.support.page;


public class MenuContextImpl implements MenuContext {

	private String contextName;
	
	public MenuContextImpl(String contextName) {
		if(contextName == null)
			throw new NullPointerException("path값을 null이 될 수 없습니다.");
		this.contextName = contextName;
	}
	
	@Override
	public String toString() {
		return this.contextName;
	}
	
}

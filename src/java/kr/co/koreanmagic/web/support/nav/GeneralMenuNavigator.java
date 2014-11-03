package kr.co.koreanmagic.web.support.nav;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import kr.co.koreanmagic.web.support.page.PageContext;
import kr.co.koreanmagic.web.support.page.MenuContext;
import kr.co.koreanmagic.web.support.page.MenuContextImpl;

public class GeneralMenuNavigator implements MenuNavigator {

	private GeneralMenuNavigator parent;
	private MenuContext pageContext;
	private List<GeneralMenuNavigator> childs;
	private boolean isCurrent;
	
	protected GeneralMenuNavigator(String path) {
		this(new MenuContextImpl(path));
	}
	protected GeneralMenuNavigator(MenuContext pageContext) {
		if(pageContext == null)
			throw new NullPointerException("PageContext는 null이 될 수 없습니다.");
		this.pageContext = pageContext;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public GeneralMenuNavigator getParent() {
		return this.parent;
	}
	@Override
	public MenuContext pageContext() {
		return this.pageContext;
	}

	
	@Override
	public boolean hasChilds() {
		if(this.childs != null && !this.childs.isEmpty()) return true;
		return false;
	}
	@Override
	public List<GeneralMenuNavigator> childs() {
		if(this.childs == null) this.childs = new ArrayList<>();
		return this.childs;
	}
	
	/*
	 * 하위 메뉴를 추가하면서, 동시에 하위 Context에 Parent를 등록한다. 양방향 설정
	 */
	public GeneralMenuNavigator addMenu(MenuContext child) {
		return addMenu(new GeneralMenuNavigator(child));
	}
	public GeneralMenuNavigator addMenu(GeneralMenuNavigator child) {
		childs().add(child);
		child.setParent(this);
		return child;
	}
	public void setParent(GeneralMenuNavigator parent) {
		this.parent = parent;
	}
	
	void on() { this.isCurrent = true; }
	void off() { this.isCurrent = false; }
	@Override
	public boolean isCurrent() {
		return this.isCurrent;
	}
	
	/*
	 * 현재 페이지로 지정할 Navigator를 찾는다.
	 */
	public void setCurrent(MenuContext context) {
		setCurrent(context.toString());
	}
	public void setCurrent(String path) {
		GeneralMenuNavigator search = this;
		setCurrent(
					search
				);
	}
	
	
	
	
	// 찾은 네이게이터를 기준으로 부모를 찾아 올라면서 모두 on() 한다.
	private void setCurrent(GeneralMenuNavigator navigator) {
		if(navigator == null) return;
		setCurrent(navigator.getParent());
		navigator.on();
	}
	
	public GeneralMenuNavigator protoFind(String path) {
		Path convertPath = Paths.get(path);
		
		if(!this.pageContext().toString().equals(
												convertPath.getName(0).toString()
												))
			return null;
		
		convertPath = convertPath.subpath(1, convertPath.getNameCount());
		
		GeneralMenuNavigator nav = this;
		for(Path p : convertPath) {
			if((nav = protoFind(nav, p.toString(), false)) == null)
				break;
		}
		return nav;
	}
	
	private GeneralMenuNavigator protoFind(GeneralMenuNavigator navigator, String path, boolean onOff) {
		
		if(navigator == null) return null;
		
		if(onOff) navigator.off();
		
		for(GeneralMenuNavigator child : navigator.childs()) {
			if(path.equals(child.pageContext().toString()))
				return child;
		}
		
		return null;
	}
	

	
	@Override
	public String toString() {
		return "[ " + pageContext().toString() + " ]";
	}
	
	
	@Override
	public String getPath() {
		return getPath(this);
	}
	private String getPath(GeneralMenuNavigator navigator) {
		
		if(navigator == null) return null;
		
		String path = navigator.pageContext().toString();
		String parentPath = getPath(navigator.getParent());
		
		if(parentPath != null)
			path = parentPath + "/" + path;
		
		return path;
	}
	
}

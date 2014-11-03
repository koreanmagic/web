package kr.co.koreanmagic.web.support.nav;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import kr.co.koreanmagic.web.support.page.PageContext;
import kr.co.koreanmagic.web.support.page.PageContextImpl;

public class GeneralNavigator implements Navigator {

	private GeneralNavigator parent;
	private PageContext pageContext;
	private List<GeneralNavigator> childs;
	private boolean isCurrent;
	
	protected GeneralNavigator(String path) {
		this(new PageContextImpl(path));
	}
	protected GeneralNavigator(PageContext pageContext) {
		if(pageContext == null)
			throw new NullPointerException("PageContext는 null이 될 수 없습니다.");
		this.pageContext = pageContext;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public GeneralNavigator getParent() {
		return this.parent;
	}
	@Override
	public PageContext pageContext() {
		return this.pageContext;
	}

	
	@Override
	public boolean hasChilds() {
		if(this.childs != null && !this.childs.isEmpty()) return true;
		return false;
	}
	@Override
	public List<GeneralNavigator> childs() {
		if(this.childs == null) this.childs = new ArrayList<>();
		return this.childs;
	}
	
	/*
	 * 하위 메뉴를 추가하면서, 동시에 하위 Context에 Parent를 등록한다. 양방향 설정
	 */
	public GeneralNavigator addMenu(PageContext child) {
		return addMenu(new GeneralNavigator(child));
	}
	public GeneralNavigator addMenu(GeneralNavigator child) {
		childs().add(child);
		child.setParent(this);
		return child;
	}
	public void setParent(GeneralNavigator parent) {
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
	public void setCurrent(PageContext context) {
		setCurrent(context.toString());
	}
	public void setCurrent(String path) {
		GeneralNavigator search = findAndInit(this, path, true);
		setCurrent(
					search
				);
	}
	// 찾은 네이게이터를 기준으로 부모를 찾아 올라면서 모두 on() 한다.
	private void setCurrent(GeneralNavigator navigator) {
		if(navigator == null) return;
		setCurrent(navigator.getParent());
		navigator.on();
	}
	
	
	/*
	 * Navigator 찾기
	 * path 주소로 찾는다. ex) path1/path2
	 */
	public GeneralNavigator find(PageContext context) {
		return find(context.toString());
	}
	public GeneralNavigator find(String path) {
		return findAndInit(this, path, false);
	}
	private GeneralNavigator findAndInit(GeneralNavigator navigator, String path, boolean onOff) {
		
		GeneralNavigator result = null;
		
		if(onOff) navigator.off();
		if(navigator.pageContext() != null && navigator.pageContext().getPath().equals(path))
			result = navigator;
			
		GeneralNavigator childResult = null;
		for(GeneralNavigator n : navigator.childs())
			if((childResult = findAndInit(n, path, onOff)) != null)
				result = childResult;
		
		return result;
	}
	
	private GeneralNavigator protoFind(String path) {
		Path convertPath = Paths.get(path);
		
		GeneralNavigator nav = this;
		for(Path p : convertPath) {
			if((nav = protoFind(nav, p.toString(), false)) == null)
					break;
		}
		return nav;
	}
	
	private GeneralNavigator protoFind(GeneralNavigator navigator, String path, boolean onOff) {
		
		if(navigator == null) return null;
		
		if(onOff) navigator.off();
		
		for(GeneralNavigator child : navigator.childs()) {
			if(path.equals(child.pageContext().toString()))
				return child;
		}
		
		return null;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageContext == null) ? 0 : pageContext.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralNavigator other = (GeneralNavigator) obj;
		if (pageContext == null) {
			if (other.pageContext != null)
				return false;
		} else if (!pageContext.equals(other.pageContext))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "[ " + pageContext().toString() + " ]";
	}
	@Override
	public String getPath() {
		return getPath(this);
	}
	private String getPath(GeneralNavigator navigator) {
		
		if(navigator == null) return null;
		
		String path = navigator.pageContext().toString();
		String parentPath = getPath(navigator.getParent());
		
		if(parentPath != null)
			path += "/" + parentPath;
		
		return path;
	}
	
}

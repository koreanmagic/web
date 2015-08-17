package kr.co.koreanmagic.web.support.nav;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web.support.nav.Navigator;

import org.apache.log4j.Logger;

/*
 * 가장 일반적으로 사용하게 될 네비게이터 구현체.
 * 
 */
public class GeneralNavigator implements Navigator {

	private GeneralNavigator parent;
	private MenuContext menuContext;
	private List<GeneralNavigator> childs;
	
	private String path;			// 일종의 캐시용도
	private boolean isCurrent;
	private boolean isHidden;
	
	Logger logger = Logger.getLogger(getClass());
	
	public GeneralNavigator(MenuContext pageContext) {
		if(pageContext == null)
			throw new NullPointerException();
		this.menuContext = pageContext;
	}
	@Override
	public MenuContext getContext() {
		return this.menuContext;
	}
	@Override
	@SuppressWarnings("unchecked")
	public GeneralNavigator getParent() {
		return this.parent;
	}

	private List<GeneralNavigator> childs() {
		if(this.childs == null) this.childs = new ArrayList<>();
		return this.childs;
	}
	
	
	@Override
	public Iterator<Navigator> iterator() {
		return new Iterator<Navigator>() {

			private int pos;
			
			@Override
			public boolean hasNext() {
				int size;
				if(childs == null || ( size = childs.size() ) == pos)
					return false;
				
				if(isHidden(pos)) {
					while(++pos < size) {
						if(!isHidden(pos))
							return true;
					}
					return false;
				}
				return true;
			}
			
			private boolean isHidden(int pos) {
				return childs.get(pos).isHidden();
			}
			

			@Override
			public Navigator next() {
				return childs.get(pos++);
			}
			
		};
	}
	
	/*
	 * 하위 메뉴를 추가하면서, 동시에 하위 Context에 Parent를 등록한다. 양방향 설정
	 */
	public<T extends GeneralNavigator> T addMenu(GeneralNavigator child) {
		childs().add(child);
		child.setParent(this);
		this.path = null;		// 다시 갱신하도록 한다.
		return (T)this;
	}
	public void setParent(GeneralNavigator parent) {
		this.parent = parent;
	}
	
	// 최상위 부모 찾기
	private GeneralNavigator getRootNavigator(GeneralNavigator parent) {
		if(parent.getParent() == null) return parent;
		return getRootNavigator(parent.getParent());
	}
	
	
	
	/*
	 * 입력 파라미터의 첫번째 path는 현재 Navigator path와 맞아야 한다.
	 */
	protected GeneralNavigator search(String path) {
		
		String[] paths = null;
		paths = path.split("/");
		
		if(!getContext().toString().equals(paths[0]))
			return null;
		
		GeneralNavigator result = this;
		
		for(int i=1, l=paths.length; i<l; i++)
			if((result = search(result, paths[i])) == null)
				return null;
		
		return result;
	}
	private GeneralNavigator search(GeneralNavigator nav, String path) {
		for(GeneralNavigator n : nav.childs())
			if(n.getContext().toString().equals(path))
				return n;
		return null;
	}


	

	
	@Override
	public String getPath() {
		if(this.path == null)
			path = "/" + getPath(this);	// 앞에 '/'를 붙여서 완성한다.   /admin/name/doto
		return path;
	}
	private String getPath(GeneralNavigator navigator) {
		
		if(navigator == null) return null;
		
		String path = navigator.getContext().toString();
		String parentPath = getPath(navigator.getParent());
		
		if(parentPath != null)
			path = parentPath + "/" + path;
		
		return path;
	}
	
	
	@Override
	public GeneralNavigator clone() {
		GeneralNavigator me = new GeneralNavigator(getContext()); 
		for(GeneralNavigator child : childs())
			me.addMenu(child.clone());
		return me;
	}
	
	
	private<T> T log(T t) {
		logger.debug(t);
		return t;
	}

	
	@Override
	public String toString() {
		return getPath();
	}

	// 찾은 네이게이터를 기준으로 부모를 찾아 올라면서 모두 on() 한다.
	private void setCurrent(GeneralNavigator navigator) throws NoAccessMenuException {
		if (navigator == null)
			return;
		setCurrent(navigator.getParent());
		navigator.on();
	}

	/*
	 * 만약 isHidden()이 false라면 에러를 발생시킨다.
	 */
	private void on() throws NoAccessMenuException {
		if(isHidden())
			throw new NoAccessMenuException(this);
		this.isCurrent = true;
	}
	private void off() {
		this.isCurrent = false;
	}
	@Override
	public boolean isOn() {
		return this.isCurrent;
	}
	@Override
	public boolean isHidden() {
		return this.isHidden;
	}
	private void setHidden(boolean hidden) {
		if(hidden) logger.debug(this);
		this.isHidden = hidden;
	}
	
	@Override
	public void reflesh(HttpServletRequest req) throws NoAccessMenuException {
		setHidden( !getContext().load(req, getPath()) );
		
		if(req.getServletPath().equals(getPath()))
			setCurrent(this);
		else
			off();
		
		/*
		 * isHidden()이 true라는 것은 어차피 하위 메뉴가 감춰진다는 뜻이다.
		 * 따라서 굳이 순회할 필요가 없다.
		 */
		if(!isHidden() && childs != null) {
			for(int i=0, l=childs.size(); i<l; i++) {
				childs.get(i).reflesh(req);
			}
		}
		
	}
	
}

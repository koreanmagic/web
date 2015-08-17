package kr.co.koreanmagic.web.support.nav;


public abstract class GenericMenuContext implements MenuContext {

	private String path;
	private String name;
	
	// path는 단일 문자로 들어온다.
	public GenericMenuContext(String path) {
		if(path == null)
			throw new NullPointerException();
		if(path.length() == 0)
			throw new IllegalArgumentException("빈 문자는 등록할 수 없습니다.");
		this.path = path;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends GenericMenuContext> T setName(String name) {
		this.name = name;
		return (T)this;
	}
	@Override
	public String getName() {
		return this.name;
	}
	
	
	@Override
	public String toString() {
		return this.path;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		GenericMenuContext other = (GenericMenuContext) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
}

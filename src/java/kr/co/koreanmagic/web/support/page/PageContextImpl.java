package kr.co.koreanmagic.web.support.page;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PageContextImpl implements PageContext {

	private String contextName;
	private String path;
	
	public PageContextImpl(String path) {
		if(path == null)
			throw new NullPointerException("path값을 null이 될 수 없습니다.");
		
		this.path = Paths.get(path).toString().replaceAll("\\\\", "/");
	}
	
	public PageContext resolve(String path) {
		Path _path = Paths.get(getPath());
		_path = _path.resolve(path);
		return new PageContextImpl(
							_path.toString().replaceAll("\\\\", "/")
						);
	}
	
	@Override
	public String getPath() {
		return this.path;
	}
	
	
	public PageContextImpl setName(String pageContextName) {
		this.contextName = pageContextName;
		return this;
	}
	@Override
	public String toString() {
		if(this.contextName == null)
			this.contextName = contextName(getPath());
		return this.contextName;
	}

	
	private String contextName(String path) {
		Path _path = Paths.get(path);
		if(_path.getNameCount() < 2) return path;
		else 
			return _path.getFileName().toString();
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
		PageContextImpl other = (PageContextImpl) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	
}

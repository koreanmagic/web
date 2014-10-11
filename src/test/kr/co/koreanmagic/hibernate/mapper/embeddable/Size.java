package kr.co.koreanmagic.hibernate.mapper.embeddable;

import javax.persistence.Embeddable;

@Embeddable
public class Size {
	
	private Integer width;
	private Integer height;
	
	public Size() {}
	public Size(Integer width, Integer height) {
		setWidth(width);
		setHeight(height);
	}
	public Size(String...s) {
		setWidth(Integer.valueOf(s[0]));
		setHeight(Integer.valueOf(s[1]));
	}
	
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Size other = (Size) obj;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return getWidth().toString() + "-" + getHeight().toString();
	}

}

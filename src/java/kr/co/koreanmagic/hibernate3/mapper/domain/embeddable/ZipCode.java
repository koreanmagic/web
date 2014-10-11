package kr.co.koreanmagic.hibernate3.mapper.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ZipCode {
	
	private String zip1 = "";
	private String zip2 = "";
	
	public ZipCode() {}
	public ZipCode(int zip1, int zip2) {
		setZip1(String.valueOf(zip1));
		setZip2(String.valueOf(zip2));
	}
	public ZipCode(String zip1, String zip2) {
		setZip1(zip1);
		setZip2(zip2);
	}
	
	@Column(nullable=false)
	public String getZip1() {
		return zip1;
	}
	public void setZip1(String zip1) {
		this.zip1 = zip1;
	}
	
	@Column(nullable=false)
	public String getZip2() {
		return zip2;
	}
	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((zip1 == null) ? 0 : zip1.hashCode());
		result = prime * result + ((zip2 == null) ? 0 : zip2.hashCode());
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
		ZipCode other = (ZipCode) obj;
		if (zip1 == null) {
			if (other.zip1 != null)
				return false;
		} else if (!zip1.equals(other.zip1))
			return false;
		if (zip2 == null) {
			if (other.zip2 != null)
				return false;
		} else if (!zip2.equals(other.zip2))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		if(getZip1() == null || getZip1().length() == 0)
			return "";
		
		return getZip1() + "-" + getZip2();
	}
	
	

}

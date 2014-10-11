package kr.co.koreanmagic.hibernate3.mapper.usertype;

import javax.persistence.Embeddable;

public class ThreeNumber {

	private String separator = "-";
	private String num1;
	private String num2;
	private String num3;
	
	public ThreeNumber() {
	}
	public ThreeNumber(Integer n1, Integer n2, Integer n3) {
		setNum1(n1.toString());
		setNum2(n2.toString());
		setNum3(n3.toString());
	}
	public ThreeNumber(String...s) {
		setNum1(s[0]);
		setNum2(s[1]);
		if(s.length > 2)	// 1588-0000 등과 같은 2자리 번호도 있을 수 있다.
			setNum3(s[2]);
	}
	
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	
	public String getNum1() {
		return num1;
	}
	public void setNum1(String num1) {
		this.num1 = num1;
	}
	public void setNum1(Integer num1) {
		this.num1 = num1.toString();
	}
	
	public String getNum2() {
		return num2;
	}
	public void setNum2(String num2) {
		this.num2 = num2;
	}
	public void setNum2(Integer num2) {
		this.num2 = num2.toString();
	}
	
	public String getNum3() {
		return num3;
	}
	public void setNum3(String num3) {
		this.num3 = num3;
	}
	public void setNum3(Integer num3) {
		this.num3 = num3.toString();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num1 == null) ? 0 : num1.hashCode());
		result = prime * result + ((num2 == null) ? 0 : num2.hashCode());
		result = prime * result + ((num3 == null) ? 0 : num3.hashCode());
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
		ThreeNumber other = (ThreeNumber) obj;
		if (num1 == null) {
			if (other.num1 != null)
				return false;
		} else if (!num1.equals(other.num1))
			return false;
		if (num2 == null) {
			if (other.num2 != null)
				return false;
		} else if (!num2.equals(other.num2))
			return false;
		if (num3 == null) {
			if (other.num3 != null)
				return false;
		} else if (!num3.equals(other.num3))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return getNum1() + separator + getNum2() + separator + getNum3();
	}
	

}
	

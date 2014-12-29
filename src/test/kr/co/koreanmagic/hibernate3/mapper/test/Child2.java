package kr.co.koreanmagic.hibernate3.mapper.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Child2 extends Parent {
	
	/*private Long id;
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}*/
	

	private String old;
	private String old1;
	private String old2;
	public String getOld() {
		return old;
	}
	public void setOld(String old) {
		this.old = old;
	}
	public String getOld1() {
		return old1;
	}
	public void setOld1(String old1) {
		this.old1 = old1;
	}
	public String getOld2() {
		return old2;
	}
	public void setOld2(String old2) {
		this.old2 = old2;
	}
	
	
}

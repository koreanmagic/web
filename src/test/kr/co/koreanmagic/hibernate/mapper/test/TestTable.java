package kr.co.koreanmagic.hibernate.mapper.test;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TestTable {
	
	private Long id;
	private String name;
	private Date time;
	
	public TestTable() {}
	public TestTable(Long id, String name) {
		setId(id); setName(name);
	}
	public TestTable(String name) {
		setName(name);
	}
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Temporal(TemporalType.TIME)
	@Generated(date="asdf", comments="", value={""})
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	

}

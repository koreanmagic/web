package kr.co.koreanmagic.hibernate3.mapper.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Parent {
	

	private Long id;
	
	@GenericGenerator(name="seq_id", strategy="kr.co.koreanmagic.hibernate3.mapper.test.Generator")
	@Id @Column(columnDefinition="BIGINT(20) AUTO_INCREMENT NOT NULL")
	@GeneratedValue(generator="seq_id")
	//@GenericGenerator(name="native",strategy="native")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	private String name;
	private String name1;
	private String name2;
	private String name3;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}

}

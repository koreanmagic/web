package kr.co.koreanmagic.hibernate3.mapper.domain.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name="bank_name")
@BatchSize(size=50)
public class BankName implements Comparable<BankName>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String memo;
	
	@Id @GeneratedValue
	@Column(name="bank_id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="bank_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="memo", columnDefinition="VARCHAR(255) DEFAULT ''")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BankName other = (BankName) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return String.format("[%s \"%s\"]", getName(), getMemo());
	}
	
	@Override
	public int compareTo(BankName o) {
		if(this.id > o.id) return 1;
		else if(this.id < o.id) return -1;
		return 0;
	}

}

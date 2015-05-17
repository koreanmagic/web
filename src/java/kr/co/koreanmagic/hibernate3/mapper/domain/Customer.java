package kr.co.koreanmagic.hibernate3.mapper.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customers")
public class Customer extends Partner {
	
	public Customer(){}
	public Customer(Long id){ super(id); }
	
	@Override
	public String toString() {
		return getName();
	}

}

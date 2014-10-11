package kr.co.koreanmagic.hibernate.mapper.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customers")
public class Customer extends Partner {
	
	
	@Override
	public String toString() {
		return String.format("[ %s (%s) ]", getName(), getCeoName());
	}
	

}

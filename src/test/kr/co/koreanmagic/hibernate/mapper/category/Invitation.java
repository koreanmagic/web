package kr.co.koreanmagic.hibernate.mapper.category;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Invitation extends ItemCategory implements Serializable {

	@Override
	public String categoryName() {
		return "초대장";
	}

}
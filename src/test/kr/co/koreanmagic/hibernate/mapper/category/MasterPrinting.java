package kr.co.koreanmagic.hibernate.mapper.category;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class MasterPrinting extends ItemCategory implements Serializable {

	@Override
	public String categoryName() {
		return "경인쇄";
	}

}
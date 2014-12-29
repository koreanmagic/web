package kr.co.koreanmagic.hibernate3.mapper.domain.category;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Sticker extends ItemCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String categoryName() {
		return "스티커";
	}

}

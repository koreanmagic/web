package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;

import org.springframework.stereotype.Component;

@Component
public class ItemCategoryService extends GenericService<ItemCategory, Long> {

	@Override
	public ItemCategory getInitalBean() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemCategory getDefaultBean() {
		throw new UnsupportedOperationException();
	}

	public ItemCategory findByName(String name) {
		return getDao().eq(false, "name", name);
	}
}

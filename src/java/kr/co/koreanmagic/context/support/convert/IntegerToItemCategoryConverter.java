package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.service.ItemCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IntegerToItemCategoryConverter implements Converter<Integer, ItemCategory> {

	@Autowired ItemCategoryService service;
	
	@Override
	public ItemCategory convert(Integer source) {
		return service.get(source.longValue());
	}

}

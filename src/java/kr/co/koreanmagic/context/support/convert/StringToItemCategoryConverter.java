package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.service.ItemCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToItemCategoryConverter implements Converter<String, ItemCategory> {

	@Autowired ItemCategoryService service;
	
	@Override
	public ItemCategory convert(String source) {
		return service.findByName(source);
	}

}

package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.service.WorkStateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToWorkStateConverter implements Converter<String, WorkState> {

	@Autowired WorkStateService service;
	
	@Override
	public WorkState convert(String source) {
		return service.findByName(source);
	}

}

package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.service.WorkStateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToWorkStateConverter implements Converter<Long, WorkState> {

	@Autowired WorkStateService service;
	
	@Override
	public WorkState convert(Long source) {
		return service.get(source);
	}

}

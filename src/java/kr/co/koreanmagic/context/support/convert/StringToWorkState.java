package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.service.WorkStateService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToWorkState implements Converter<String, WorkState> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired WorkStateService service;
	
	@Override
	public WorkState convert(String source) {
		return service.get(Integer.valueOf(source));
	}
}

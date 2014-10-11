package kr.co.koreanmagic.web.controller.convertor;

import kr.co.koreanmagic.web.domain.WorkCondition;
import kr.co.koreanmagic.web.service.WorkService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkConditionByString implements Converter<String, WorkCondition>{

	@Autowired private WorkService workService;
	Logger log = Logger.getLogger(getClass());
	
	@Override
	public WorkCondition convert(String source) {
		return workService.getWorkCondition(Integer.valueOf(source));
	}

}

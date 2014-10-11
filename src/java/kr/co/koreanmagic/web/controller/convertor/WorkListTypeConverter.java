package kr.co.koreanmagic.web.controller.convertor;

import kr.co.koreanmagic.web.domain.WorkListType;
import kr.co.koreanmagic.web.service.WorkService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkListTypeConverter implements Converter<String, WorkListType> {
	
	Logger logger = Logger.getLogger(getClass());
	@Autowired private WorkService workService;
	
	@Override
	public WorkListType convert(String source) {
		return workService.getWorkListType(Integer.valueOf(source));
	}

}

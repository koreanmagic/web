package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.service.WorkService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToWork implements Converter<String, Work> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired WorkService service;
	
	@Override
	public Work convert(String source) {
		return service.load(source);
	}
}

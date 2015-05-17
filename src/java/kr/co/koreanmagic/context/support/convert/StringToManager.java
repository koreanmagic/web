package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.service.ManagerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToManager implements Converter<String, Manager> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired ManagerService service;
	
	@Override
	public Manager convert(String source) {
		if(source.equals("null")) return null;
		return service.load(Long.valueOf(source));
	}
}

package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BizClass;
import kr.co.koreanmagic.service.BizClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBizClassConverter implements Converter<String, BizClass> {

	@Autowired BizClassService service;
	
	@Override
	public BizClass convert(String source) {
		return service.findByName(source);
	}

}

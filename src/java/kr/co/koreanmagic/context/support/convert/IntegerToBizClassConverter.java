package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BizClass;
import kr.co.koreanmagic.service.BizClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IntegerToBizClassConverter implements Converter<Integer, BizClass> {

	@Autowired BizClassService service;
	
	@Override
	public BizClass convert(Integer source) {
		return service.get(source.longValue());
	}

}

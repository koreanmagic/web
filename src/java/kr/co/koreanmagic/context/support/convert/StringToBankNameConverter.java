package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;
import kr.co.koreanmagic.service.BankNameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBankNameConverter implements Converter<String, BankName> {

	@Autowired BankNameService service;
	
	@Override
	public BankName convert(String source) {
		return service.findByName(source);
	}

}

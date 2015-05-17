package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.service.BankService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToBank implements Converter<String, Bank> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired BankService service;
	
	@Override
	@Transactional
	public Bank convert(String source) {
		return service.load(Long.valueOf(source));
	}
}

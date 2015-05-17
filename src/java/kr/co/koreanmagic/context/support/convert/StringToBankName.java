package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;
import kr.co.koreanmagic.service.BankNameService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToBankName implements Converter<String, BankName> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired BankNameService service;
	
	@Override
	@Transactional
	public BankName convert(String source) {
		return service.get(Long.valueOf(source));
	}
}

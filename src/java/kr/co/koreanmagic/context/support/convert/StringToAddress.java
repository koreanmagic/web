package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.service.AddressService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToAddress implements Converter<String, Address> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired AddressService service;
	
	@Override
	@Transactional
	public Address convert(String source) {
		if(source.equals("null")) return null;
		return service.load(Long.valueOf(source));
	}
}

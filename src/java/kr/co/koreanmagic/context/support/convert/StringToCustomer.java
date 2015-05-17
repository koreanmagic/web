package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;
import kr.co.koreanmagic.service.CustomerService;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToCustomer implements Converter<String, Customer> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired CustomerService service;
	
	@Override
	public Customer convert(String source) {
		return service.load(Long.valueOf(source));
	}
}

package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NameToCustomerConverter implements Converter<String, Customer> {

	@Autowired CustomerService service;
	
	@Override
	public Customer convert(String source) {
		return service.findByName(source);
	}

}

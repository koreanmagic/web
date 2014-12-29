package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerService extends GenericService<Customer, Long> {

	@Override
	public Customer getInitalBean() {
		return new Customer();
	}

	@Override
	public Customer getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	
	public Customer findByName(String name) {
		return getDao().eq(false, "name", name);
	}
	

}

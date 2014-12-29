package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;

import org.springframework.stereotype.Component;

@Component
public class AddressService  extends GenericService<Address, Long>{

	@Override
	public Address getInitalBean() {
		return new Address();
	}

	@Override
	public Address getDefaultBean() {
		throw new UnsupportedOperationException();
	}

}

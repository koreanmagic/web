package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;

import org.springframework.stereotype.Component;

@Component
public class BankNameService extends GenericService<BankName, Long> {

	@Override
	public BankName getInitalBean() {
		return new BankName();
	}

	@Override
	public BankName getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	public BankName findByName(String name) {
		return getDao().eq(true, "name", name);
	}

}

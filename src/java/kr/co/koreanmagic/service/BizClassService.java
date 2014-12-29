package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BizClass;

import org.springframework.stereotype.Component;

@Component
public class BizClassService extends GenericService<BizClass, Long> {
	
	@Override
	public BizClass getInitalBean() {
		return new BizClass();
	}

	@Override
	public BizClass getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	public BizClass findByName(String name) {
		return getDao().eq(false, "name", name);
	}

}

package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;

import org.springframework.stereotype.Component;

@Component
public class SubcontractorService extends GenericService<Subcontractor, Long> {

	@Override
	public Subcontractor getInitalBean() {
		return new Subcontractor();
	}

	@Override
	public Subcontractor getDefaultBean() {
		throw new UnsupportedOperationException();
	}

	
	public Subcontractor findByName(String name) {
		return getDao().eq(false, "name", name);
	}
	
}

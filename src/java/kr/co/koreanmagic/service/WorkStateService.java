package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;

import org.springframework.stereotype.Component;

@Component
public class WorkStateService extends GenericService<WorkState, Integer> {

	
	@Override
	public WorkState getInitalBean() {
		return get(1);
	}

	@Override
	public WorkState getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	public WorkState findByName(String name) {
		return getDao().eq(false, "name", name);
	}
}

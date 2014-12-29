package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkReference;


public class WorkReferenceService extends GenericService<WorkReference, Long> {

	@Override
	public WorkReference getInitalBean() {
		return new WorkReference();
	}

	@Override
	public WorkReference getDefaultBean() {
		throw new UnsupportedOperationException();
	}

}

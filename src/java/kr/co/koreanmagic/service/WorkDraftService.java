package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkDraft;

public class WorkDraftService extends GenericService<WorkDraft, Long> {

	@Override
	public WorkDraft getInitalBean() {
		return new WorkDraft();
	}

	@Override
	public WorkDraft getDefaultBean() {
		throw new UnsupportedOperationException();
	}

}

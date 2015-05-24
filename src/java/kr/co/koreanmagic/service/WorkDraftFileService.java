package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkDraftFile;

public class WorkDraftFileService extends WorkFileService<WorkDraftFile> {
	
	@Override
	public WorkDraftFile getInitalBean() {
		return new WorkDraftFile();
	}

	@Override
	public WorkDraftFile getDefaultBean() {
		throw new UnsupportedOperationException();
	}


	

}

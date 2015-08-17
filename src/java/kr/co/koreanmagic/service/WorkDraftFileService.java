package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkDraftFile;

import org.springframework.stereotype.Component;

@Component
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

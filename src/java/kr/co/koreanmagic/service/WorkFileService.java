package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkFile;

public class WorkFileService extends GenericService<WorkFile, Long> {

	@Override
	public WorkFile getInitalBean() {
		return new WorkFile();
	}

	@Override
	public WorkFile getDefaultBean() {
		throw new UnsupportedOperationException();
	}

}

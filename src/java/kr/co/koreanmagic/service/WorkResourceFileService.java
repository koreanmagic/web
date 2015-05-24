package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkResourceFile;

import org.springframework.stereotype.Component;

@Component
public class WorkResourceFileService extends WorkFileService<WorkResourceFile> {
	
	@Override
	public WorkResourceFile getInitalBean() {
		return new WorkResourceFile();
	}

	@Override
	public WorkResourceFile getDefaultBean() {
		throw new UnsupportedOperationException();
	}


}

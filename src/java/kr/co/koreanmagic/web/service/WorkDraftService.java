package kr.co.koreanmagic.web.service;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkDraftMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkData;
import kr.co.koreanmagic.web.domain.WorkDraft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkDraftService extends ResourceDownloader<WorkDraft> {

	@Autowired
	public WorkDraftService(WorkDraftMapper mapper) {
		super(mapper);
	}
	
	

	@Override
	String getDownloadFileName(Work work, String originalFileName) {
		return work.toString();
	}

	@Override
	WorkData getWorkData() {
		return new WorkDraft();
	}

	
}

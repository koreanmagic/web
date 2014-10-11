package kr.co.koreanmagic.web.service;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkFileMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkData;
import kr.co.koreanmagic.web.domain.WorkFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class WorkFileService extends ResourceDownloader<WorkFile> {
	
	@Autowired
	protected WorkFileService(WorkFileMapper mapper) {
		super(mapper);
	}


	@Override
	WorkData getWorkData() {
		return new WorkFile();
	}

	@Override
	String getDownloadFileName(Work work, String originalFileName) {
		return work.toString();
	}

	
}

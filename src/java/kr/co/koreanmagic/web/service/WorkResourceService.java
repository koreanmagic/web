package kr.co.koreanmagic.web.service;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkResourceMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkData;
import kr.co.koreanmagic.web.domain.WorkResource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class WorkResourceService extends ResourceDownloader<WorkResource> {
	
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	protected WorkResourceService(WorkResourceMapper mapper) {
		super(mapper);
	}
	
	@Override
	WorkData getWorkData() {
		return new WorkResource();
	}

	@Override
	String getDownloadFileName(Work work, String originalFileName) {
		return originalFileName;
	}

}

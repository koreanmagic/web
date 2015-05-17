package kr.co.koreanmagic.service;

import kr.co.koreanmagic.context.support.file.FileDailyDownloader;
import kr.co.koreanmagic.context.support.file.FileDownloader;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkDraftFile;

import org.springframework.beans.factory.annotation.Value;

public class WorkDraftFileService extends WorkFileService<WorkDraftFile> {
	
	private FileDownloader downloader;
	
	@Override
	public WorkDraftFile getInitalBean() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WorkDraftFile getDefaultBean() {
		throw new UnsupportedOperationException();
	}

	@Value("${hancome.workfile.root}")
	protected void setRootPath(String pathSource) {
		this.downloader = new FileDailyDownloader( pathSource );
	}

	@Override
	FileDownloader getDownloader() {
		return downloader;
	}

	

}

package kr.co.koreanmagic.service;

import kr.co.koreanmagic.context.support.file.FileDailyDownloader;
import kr.co.koreanmagic.context.support.file.FileDownloader;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkResourceFile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkResourceFileService extends WorkFileService<WorkResourceFile> {
	
	private FileDownloader downloader;
	
	@Override
	public WorkResourceFile getInitalBean() {
		return new WorkResourceFile();
	}

	@Override
	public WorkResourceFile getDefaultBean() {
		throw new UnsupportedOperationException();
	}

	@Value("${hancome.workfile.root}")
	protected void setRootPath(String pathSource) {
		this.downloader = new FileDailyDownloader( pathSource ).setSubdir( "files" );
	}

	@Override
	FileDownloader getDownloader() {
		return downloader;
	}

}

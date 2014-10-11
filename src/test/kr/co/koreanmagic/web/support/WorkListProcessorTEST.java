package kr.co.koreanmagic.web.support;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkDraftMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkFileMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkResourceMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkData;
import kr.co.koreanmagic.web.domain.WorkFile;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.service.WorkDraftService;
import kr.co.koreanmagic.web.service.WorkFileService;
import kr.co.koreanmagic.web.service.WorkResourceService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class WorkListProcessorTEST {

	
	Logger logger = Logger.getLogger("test");
	
	private final String root = "G:/app/Projects/test";
	private final Path testFile = Paths.get(root, "dailyout[1].log");
	private final Path testFile2 = Paths.get(root, "watcher.log");
	
	private WorkListProcessor ps;
	
	private final String WORK_ID = "20140101001";
	private final int LIST_SIZE = 10;
	
	@Before
	public void init() {
		ps = new WorkListProcessor();
		ps.setRootPath(Paths.get(root));
		ps.setWorkResourceService(getResourceService(WORK_ID, LIST_SIZE));
		ps.setWorkFileService(getFileService(WORK_ID, LIST_SIZE));
		ps.setWorkDraftService(getDraftService(WORK_ID, LIST_SIZE));
	}
	
	@Test
	public void test() throws Exception {
		MultipartFile multipartFile1 = getMultiFile(testFile, 20, testFile.getFileName().toString());
		MultipartFile multipartFile2 = getMultiFile(testFile2, 20, testFile2.getFileName().toString());
		List<MultipartFile> list = Arrays.asList(multipartFile1, multipartFile2);
		Work work = getWork(WORK_ID, "hancomeFile");
		List<WorkResource> result = ps.resourceDownload(work, list);
		
		logger.debug("반환된 객체 객 수 : " + result.size());
		int count = 1;
		for(WorkData d : result) {
			logger.debug(count++ + ") " + d);
		}
	}
	
	@Test
	public void test2() throws Exception {
		MultipartFile multipartFile = getMultiFile(testFile, 20, testFile.getFileName().toString());
		Work work = getWork(WORK_ID, "hancomeFile");
		WorkFile result = ps.fileDownload(work, multipartFile);
		logger.debug(result);
	}
	
	
	/* MultipartFile */
	private MultipartFile getMultiFile(Path target, long size, String originalFileName) throws IOException {
		MultipartFile file = mock(MultipartFile.class);
		when(file.getInputStream()).thenReturn(Files.newInputStream(target));
		when(file.getSize()).thenReturn(size);
		when(file.getOriginalFilename()).thenReturn(originalFileName);
		return file;
	}
	
	/* Work */
	private Work getWork(String id, String toString) {
		Work work = mock(Work.class);
		when(work.toString()).thenReturn(toString);
		when(work.getId()).thenReturn(id);
		when(work.getInsertTime()).thenReturn(new Timestamp(new Date().getTime()));
		return work;
	}
	
	/* WorkResourceService */
	private WorkResourceService getResourceService(String work_id, int listSize) {
		WorkResourceService service = new WorkResourceService();
		WorkResourceMapper mapper = mock(WorkResourceMapper.class);
		when(mapper.count(work_id)).thenReturn(listSize);
		service.setMapper(mapper);
		return service;
	}
	/* WorkDraftService */
	private WorkDraftService getDraftService(String work_id, int listSize) {
		WorkDraftService service = new WorkDraftService();
		WorkDraftMapper mapper = mock(WorkDraftMapper.class);
		when(mapper.count(work_id)).thenReturn(listSize);
		service.setMapper(mapper);
		return service;
	}
	/* WorkFileService */
	private WorkFileService getFileService(String work_id, int listSize) {
		WorkFileService service = new WorkFileService();
		WorkFileMapper mapper = mock(WorkFileMapper.class);
		when(mapper.count(work_id)).thenReturn(listSize);
		service.setMapper(mapper);
		return service;
	}
	

}

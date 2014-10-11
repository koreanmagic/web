package kr.co.koreanmagic.web.support;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import kr.co.koreanmagic.commons.KoBeanUtils;
import kr.co.koreanmagic.commons.KoDateUtils;
import kr.co.koreanmagic.commons.KoFileUtils;
import kr.co.koreanmagic.web.controller.support.BeanProcessor;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkCondition;
import kr.co.koreanmagic.web.domain.WorkData;
import kr.co.koreanmagic.web.domain.WorkDraft;
import kr.co.koreanmagic.web.domain.WorkFile;
import kr.co.koreanmagic.web.domain.WorkListType;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.service.ResourceDownloader;
import kr.co.koreanmagic.web.service.WorkDraftService;
import kr.co.koreanmagic.web.service.WorkFileService;
import kr.co.koreanmagic.web.service.WorkResourceService;
import kr.co.koreanmagic.web.service.WorkService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WorkListProcessor implements BeanProcessor<Work> {
	
	public static final int WORK_FILE = 1;
	public static final int DRAFT = 2;
	public static final int RESOURCE = 3;
	
	private Logger logger = Logger.getLogger(getClass());
	
	private @Value("${Work.file.rootpath}")  String root;
	private Path rootPath;
	
	
	private static final String RESOURCE_REQUEST_PATH = "/work/";  //리소스 요청주소
	
	private static final String WORK_DRAFT_DIR = "img";
	private static final String WORK_RESOURCE_DIR = "file";
	
	private static Map<String, Path> cache = new HashMap<>();
	// 캐시
	static Path cache(String date, Path root) { return cache.put(date, root); }
	static Path cache(String date) { return cache.get(date); }
	
	
	@Autowired private WorkService workService;
	@Autowired private WorkFileService workFileService;
	@Autowired private WorkDraftService workDraftService;
	@Autowired private WorkResourceService workResourceService;
	
	
	/* 테스트 용 */
	public void setWorkResourceService(WorkResourceService workResourceService) {
		this.workResourceService = workResourceService;
	}
	public void setWorkDraftService(WorkDraftService workDraftService) {
		this.workDraftService = workDraftService;
	}
	public void setWorkFileService(WorkFileService workFileService) {
		this.workFileService = workFileService;
	}
	
	public Path getRootPath() { return this.rootPath; }
	
	@Override public Class<Work> getType() { return Work.class; }
	
	@PostConstruct public void init() {
		rootPath = Paths.get(root);
	}
	
	public void setRootPath(Path path) { this.rootPath = path; }

	/*
	 *  빈으로 파일 저장할 폴더 만들기
	 *  썸네일폴더까지 만든 후 그 상위폴더 반환 (실제로 파일이 있어야할 폴더)
	 *  반환 값은 ex) 2014/2
	 *  
	 */
	Path getDir(Work work) throws IOException {
		String dateStr = convertDateToPath(work);
		Path result = null;
		if((result = cache(dateStr)) != null)	// 캐시에 있다면 반환
			return result;
		
		result = createDir(dateStr);
		cache(dateStr, result); 				// 캐시 저장
		return result;
	}
	
	// 날짜를 분할해 디렉터리 만들기
	private Path createDir(String dateStr) throws IOException {
		Path root = createDir0(rootPath, dateStr); 	// convertDateToPath에서 yyyy-MM-dd를 yyyy-MM/dd로 바꿔준다. 때문에 두 개의 디렉터리가 생성된다.
		createDir0(root, WORK_DRAFT_DIR); 			// 시안폴더 생성
		createDir0(root, WORK_RESOURCE_DIR);		// 참고파일 생성
		return root;
	}
	// 디렉터리 만들기
	private Path createDir0(Path _rootPath, String newDir) throws IOException {
		Path target = _rootPath.resolve(newDir);
		return Files.createDirectories(target);
	}
	
	// 게시물 날짜를 Parsing해서 폴더구조를 추론한다.
	private String convertDateToPath(Work work) {
		StringBuilder dateString = new StringBuilder(KoDateUtils.getSqlStyle(work.getInsertTime()));
		dateString.replace(7, 8, "/");		// 2014-01-01 --> 2014-01/01
		return dateString.toString();
	}
		
	// 시안을 저장할 폴더를 반환해주는 메서드
	public Path getThumbnailDir(Work work) throws IOException { 
		String date = convertDateToPath(work); 		// 키 값을 얻어온다
		Path result = null;
		if((result = cache(date)) == null) 			// 캐시에 없으면
			result = createDir(date); 				//새로 만든다
		return result.resolve(WORK_DRAFT_DIR); 		// 파일 폴더에 img를 더해서 내보낸다.
	}
	

	
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ Download ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	// 멀티파트 파일이 있는지 확인 ~~ 없으면 -1
	private boolean existsFile(List<MultipartFile> files) {
		int size = files.size();
		if (size == 0)
			return false;
		if (size == 1 && files.get(0).getSize() == 0)
			return false;
		return true;
	}
	
	/* *********  ▼ 참고파일 다운로드 ▼  ********* */
	@SuppressWarnings("unchecked")
	public List<WorkResource> resourceDownload(Work work, List<MultipartFile> files) throws IOException {
		if(!existsFile(files)) return Collections.EMPTY_LIST;
		List<WorkResource> result = new ArrayList<>(files.size());
		for(MultipartFile file : files) {
			result.add((WorkResource)fileDownload(work, file.getInputStream(),
													file.getOriginalFilename(), workResourceService,
													WORK_RESOURCE_DIR));
		}
		return result;
	}

	/* *********  ▼ 시안파일 다운로드 ▼  ********* */
	@SuppressWarnings("unchecked")
	public List<WorkDraft> draftDownload(Work work, List<MultipartFile> files) throws IOException {
		if(!existsFile(files)) return Collections.EMPTY_LIST;
		List<WorkDraft> result = new ArrayList<>(files.size());
		for(MultipartFile file : files) {
			result.add((WorkDraft)fileDownload(work, file.getInputStream(),
												file.getOriginalFilename(), workDraftService,
												WORK_DRAFT_DIR));
		}
		return result;
	}
	
	/* *********  ▼ 원본파일 다운로드 ▼  ********* */
	public WorkFile fileDownload(Work work, MultipartFile file) throws IOException {
		if(file.getSize() == 0) return null;
		return (WorkFile)fileDownload(work, file.getInputStream(), file.getOriginalFilename(), workFileService);
	}
	
	
	WorkData fileDownload(Work work, InputStream from, String originalFileName, ResourceDownloader<?> service) throws IOException {
		return fileDownload(work, from, originalFileName, service, "");
	}
	WorkData fileDownload(Work work, InputStream from, String originalFileName, ResourceDownloader<?> service, String subDir) throws IOException {
		Path root = getDir(work);									// 캐시 키 값
		Path parentPath = KoFileUtils.lastIndexSubPath(root, 2).resolve(subDir); 	// 부모 폴더
		return service.fileDownload(work, from, rootPath, parentPath, originalFileName);
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▲ Download ▲  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	
	
	
	// ReflectionUtils
	static final Map<String, PropertyDescriptor> workFileDescriptor = KoBeanUtils.getPropertyDescriptor(WorkFile.class);
	static final Map<String, PropertyDescriptor> workDraftDescriptor = KoBeanUtils.getPropertyDescriptor(WorkDraft.class);
	static final Map<String, PropertyDescriptor> workResourceDescriptor = KoBeanUtils.getPropertyDescriptor(WorkResource.class);
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 각종 데이터자원 ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 컨디션 정보
	public List<WorkCondition> getConditions() {
		return workService.workConditionList();
	}
	// listType정보
	public List<WorkListType> getWorkListTypes() {
		return workService.workListTypes();
	}
	// 현황정보
	public Map<String, Integer> getWorkListStats() {
		return workService.workListStats();
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ 뷰 템플릿 위한 메서드 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	public int getWorkFileSize(Work work) {
		return this.workFileService.count(work);
	}
	public int getWorkDraftSize(Work work) {
		return this.workDraftService.count(work);
	}
	public int getWorkResourceSize(Work work) {
		return this.workResourceService.count(work);
	}
	
	//인쇄파일 리스트
	public List<WorkFile> getWorkFiles(Work work) {
		return workFileService.get(work);
	}
	public List<WorkDraft> getWorkDrafts(Work work) {
		return workDraftService.get(work);
	}
	public List<WorkResource> getWorkResources(Work work) {
		return workResourceService.get(work);
	}
	
	// 다운로드 횟수 올리기
	public int workFileCountUp(int id) {
		return this.workFileService.addCount(id);
	}
	public int workDraftCountUp(int id) {
		return this.workDraftService.addCount(id);
	}
	public int workResourceCountUp(int id) {
		return this.workResourceService.addCount(id);
	}
	
	// 시안 주소
	public String getDraftSrc(WorkDraft draft) {
		return RESOURCE_REQUEST_PATH + draft.getParentPath() + "/" + draft.toString();
	}
	
	//시안파일 이미지 주소
	public String getTuhumbnailSrc(Work work) {
		List<WorkDraft> list = workDraftService.get(work);
		if(list.size() < 1) // 시안이 없으면
			return "/img/default.jpg";
		WorkDraft draft = list.get(list.size() - 1);
		return RESOURCE_REQUEST_PATH + draft.getParentPath() + "/" + draft.toString();
	}
	
	// 리소스 다운로드 주소
	public String getResourceSrc(Object target) {
		Map<String, PropertyDescriptor> discriptors = findDiscriptors(target);
		if(discriptors == null) throw new RuntimeException("WorkFile, WorkDraft, WorkResource 구현체를 넣어주세요! \n -->" + target);
		
		String parentPath = KoBeanUtils.invokeReadMethod(discriptors.get("parentPath"), target);
		String id = String.valueOf(KoBeanUtils.invokeReadMethod(discriptors.get("id"), target));
		
		// 파일 요청 주소
		return RESOURCE_REQUEST_PATH + parentPath + "/" + target.toString() + 
				"?fileType=" + target.getClass().getSimpleName() + "&id=" + id;
	}
	
	
	// 디스크립터 찾기
	private Map<String, PropertyDescriptor> findDiscriptors(Object target) {
		if(target instanceof WorkFile)
			return workFileDescriptor;
		else if(target instanceof WorkDraft)
			return workDraftDescriptor;
		else if(target instanceof WorkResource)
			return workResourceDescriptor;
		return null;
	}

}

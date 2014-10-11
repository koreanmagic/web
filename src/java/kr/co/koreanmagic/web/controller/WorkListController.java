package kr.co.koreanmagic.web.controller;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.koreanmagic.web.controller.argresolver.BoardListSupport;
import kr.co.koreanmagic.web.controller.propertyeditor.WorkSizeEditor;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkCondition;
import kr.co.koreanmagic.web.domain.WorkDraft;
import kr.co.koreanmagic.web.domain.WorkFile;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.service.WorkDraftService;
import kr.co.koreanmagic.web.service.WorkFileService;
import kr.co.koreanmagic.web.service.WorkResourceService;
import kr.co.koreanmagic.web.service.WorkService;
import kr.co.koreanmagic.web.support.WorkBoardList;
import kr.co.koreanmagic.web.support.WorkListProcessor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/worklist/")
@SessionAttributes({"command", "workFiles", "workDrafts"})
public class WorkListController {
	
	private Logger logger = Logger.getLogger(getClass());
	private static final String ROOT_PATH = "/worklist/";
	private static final String FREEMARKER_PREFIX = "default";
	
	@Autowired private WorkService workService;
	@Autowired private WorkFileService workFileService;
	@Autowired private WorkDraftService workDraftService;
	@Autowired private WorkResourceService workResourceService;
	
	@Autowired private WorkListProcessor beanProcessor;
	
	
	
	@InitBinder
	public void webInit(WebDataBinder binder) {
		PropertyEditor editor = new WorkSizeEditor();
		binder.registerCustomEditor(String.class, "size", editor);
		binder.registerCustomEditor(String.class, "bleedSize", editor);
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ GLOBAL METHOD ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	// 패스 만들기
	private String redirect(Object... paths) {
		return "redirect:" + ROOT_PATH + addPath(paths);
	}

	private String getPath(Object... paths) {
		return FREEMARKER_PREFIX + ROOT_PATH + addPath(paths);
	}

	private String addPath(Object... paths) {
		return StringUtils.join(paths, "/");
	}

	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▲ GLOBAL METHOD ▲  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  입력  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */ 
	@RequestMapping(value="insert", method=RequestMethod.GET)
	public String insert(ModelMap model) {
		Work work = new Work();
		work.setCondition(workService.getWorkCondition(1));
		model.put("command", work);
		return getPath("insert");
	}
	
	
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String insert(@ModelAttribute("command") Work work,
						@RequestParam(value="resources", required=false) List<MultipartFile> resources,
						SessionStatus sessionStatus
						) throws IOException {
		
		workService.setUniqueId(work); // sequence 키를 받아온다.
		List<WorkResource> workResourceList = beanProcessor.resourceDownload(work, resources); // 리소스파일 다운로드
		
		// 반드시 work가 먼저 입력되어야 한다. work가 work_resource의 부모테이블
		if(workService.insert(work) != 1) throw new RuntimeException("INSERT ERROR!! --> " + work);
		if(workResourceList.size() != 0 && workResourceService.insert(workResourceList) != resources.size())
			throw new RuntimeException("다운로더 입력 실패!");
		
		sessionStatus.setComplete(); // 세션제거
		return redirect("list", "condition/1");
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  수정  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 쿼리에 work는 반드시 있어야 한다.
	@RequestMapping(value="modify", method=RequestMethod.GET)
	public String modify(@RequestParam("id") Work work,
						@RequestParam(value="condition", required=false) WorkCondition condition,
						ModelMap model) {
		
		int relatedConId = work.getCondition().getId(); // 원래있던 페이지(컨디션 넘버) 저장
		if(condition != null) // 컨디션이 있을 경우, 컨디션만 바꿔달라는 요청임. 바로 바꾼다.
			if(work.getCondition().compareTo(condition) == -1) { // 컨디션이 낮은 번호에서 높은 번호로 갈때만 수정
				if(condition.getId() == 6 && work.getListType().getId() == 3) // 납품으로 넘어가는 중이고, 긴급상태였다면
					workService.updateNormal(work); // 노멀로 돌린다
				updateCondition(work, condition);
				return redirect("list/condition", relatedConId); // 원래 있던 페이지로 돌려보낸다.
			}
		
		model.put("command", work);
		model.put("workFiles", workFileService.get(work)); // WorkFile이 함께 나간다.
		model.put("workDrafts", workDraftService.get(work)); // WorkDraft이 함께 나간다.
		
		return getPath("insert");
	}
	
	private void updateCondition(Work work, WorkCondition condition) {
		work.setCondition(condition);
		if(workService.update(work) != 1) throw new RuntimeException("업데이트 에러");
		workService.refesh(work); // 수정한 날짜 갱신
	}
	
	
	
	@RequestMapping(value="modify", method=RequestMethod.POST)
	public String modify(@ModelAttribute("command") Work work,
						@RequestParam("file") MultipartFile file,
						@RequestParam("draft") List<MultipartFile> drafts,
						@RequestParam("resources") List<MultipartFile> resources
						) throws IOException {
		
		int updateSize = 0;

		/* WorkResource (리소스파일) */
		List<WorkResource> workResourceList = beanProcessor.resourceDownload(work, resources);
		updateSize = workResourceList.size();
		if(updateSize != 0 && workResourceService.insert(workResourceList) != updateSize) 	// 데이터베이스에 입력
			throw new RuntimeException("리소스 다운로더 입력 실패!");

		/* WorkDraft (시안파일) */
		List<WorkDraft> workDraftList = beanProcessor.draftDownload(work, drafts);
		updateSize = workDraftList.size();
		if(updateSize != 0) {
			if(workDraftService.insert(workDraftList) != updateSize) 						// 데이터베이스에 입력
				throw new RuntimeException("시안 다운로더 입력 실패!");
			if(work.getCondition().getId() == 1) {											// 컨디션이 1이면 시안검토로 바꾼다.
				work.setCondition(workService.getWorkCondition(2)); 						// 2(시안검토)
			}
		}

		/* WorkFile (원본파일) */
		if(file.getSize() > 0) { 										// 인쇄 파일이 있으면
			WorkFile workFile = beanProcessor.fileDownload(work, file);
			workFileService.insert(workFile);
			
			if(work.getCondition().getId() < 3) 						// 시안완료 전이면 시안완료로 바꾼다.
				work.setCondition(workService.getWorkCondition(3)); 	// 컨디션을 3(시안완료로 상승)
		}
		
		workService.update(work);		// 워크리스트 업데이트
		workService.refesh(work);		// 업데이트 타임 갱신
		workService.readCheckOn(work); 	// 리드체크 ON
		
		return redirect("list/condition", work.getCondition().getId());
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  개별 상세페이지  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	@RequestMapping(value="view/{id}", method=RequestMethod.GET)
	public String view(@PathVariable("id") Work work, ModelMap model) {
		model.put("work", work);
		model.put("workFiles", workFileService.get(work));			// 원본파일 리스트
		model.put("workDrafts", workDraftService.get(work));		// 시안파일 리스트
		model.put("workResources", workResourceService.get(work));	// 참고파일 리스트
		workService.read(work); // 조회수 증가시키기
		workService.readCheckOff(work); // 리드체크 없앰
		return getPath("view");
	}
	
	
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  검색  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	@RequestMapping(value="search", method=RequestMethod.GET)
	public String search(@BoardListSupport WorkBoardList boardList, // 게시판 관련 객체
						@RequestParam("column") String column, // 검색할 컬럼
						@RequestParam("search") String searchKey, // 키워드
						@RequestParam("condition") WorkCondition condition, // 컨디션
						ModelMap model
						) {
		
		int totalNum = workService.searchByCustomerLen(condition, searchKey);
		
		if(totalNum == 0)					// 검색결과 없을 시
			return getPath("failer");
		
		int interval = boardList.getPage() - 1;
		int size = boardList.getSize();
		interval = interval * size;
		
		List<Work> result = null;
		if(column.equals("customer")) {
			result = workService.searchByCustomer(condition, searchKey, interval, size);
		}
		
		/* 긴급 게시물 */
		List<Work> emerWorks = getEmerWorks(condition);
		model.put("emergencytWorks", emerWorks);
		
		boardList.setTotalRecordNum(totalNum);
		boardList.setList(result);
		model.put("currentCondition", condition); // 현재 컨디션 넘버를 넣어준다.
		return getPath("list");
	}
	
	

		
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  리스트  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 그냥 리스트는 대기열 리스트로 보낸다.
	@RequestMapping(value="list", method=RequestMethod.GET)
	public String list() {
		return redirect("list", "condition", 1);
		
	}
	
	/* 긴급 게시물 */
	private List<Work> getEmerWorks(WorkCondition condition) {
		if(condition.getId() != 7) return workService.getEmergencytWorks(condition);
		else return new ArrayList<Work>();
	}
	
	@RequestMapping(value="list/condition/{value}", method=RequestMethod.GET)
	public String list(@BoardListSupport WorkBoardList boardList, // 게시판 관련 객체
						@PathVariable(value="value") WorkCondition condition,
						ModelMap model
						) {
		
		int con_id = condition.getId();
		model.put("currentCondition", condition); // 현재 컨디션 넘버를 넣어준다.
		
		/* 긴급 게시물 */
		List<Work> emerWorks = getEmerWorks(condition);
		model.put("emergencytWorks", emerWorks);
		
		if(!fillListByCondition(boardList, condition) && emerWorks.size() == 0) // 파일도 없고. 긴급게시물도 없으면
			return redirect("list", "condition", con_id + 1); // 컨디션을 1씩 높이면서 순회한다.
		
		return getPath("list");
	}
	
	private boolean fillListByCondition(WorkBoardList boardList, WorkCondition condition) {
		int totalNum = workService.getCountListByCondition(condition);	// 컨디션 별 리스트 갯수
		if(totalNum < 1) {
			//boardList.setList(new ArrayList<Work>()); // 프리마커쪽 루프의 NullPointException을 방지하기 위한 빈 리스트 생성
			return false;
		}
		boardList.setTotalRecordNum(totalNum);
		
		int interval = boardList.getPage() - 1;
		int size = boardList.getSize();
		List<Work> works =  workService.getListByCondition(condition, interval*size, size);
		boardList.setList(works);
		return true;
	}
		
	
	
}

package kr.co.koreanmagic.web2.controller.work;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.service.WorkStateService;
import kr.co.koreanmagic.service.boardlist.WorkBoard;
import kr.co.koreanmagic.service.example.WorkExample;
import kr.co.koreanmagic.web2.controller.work.WorkController.WorkControllerMember;
import kr.co.koreanmagic.web2.servlet.exception.NoSearchListException;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WorkList extends WorkControllerMember {

	
	// 작업상황 바꾸기
	@RequestMapping(value="/set/state/{workId}/{state}")
	public String setState(@PathVariable("workId") Work work,
								@PathVariable("state") WorkState state,
								@RequestHeader("referer") String referer,
								ModelMap model
							) {
		service.changeState(work, state);
		model.clear();	// Controller의 ModelAttribute가 저장하고 있는 필요없는 정보를 삭제한다.
		return "redirect:" + referer;
	}
	// 작업상황 바꾸기
	@RequestMapping(value="/set/workType/{workId}/{workType}")
	public String setWorkTyoe(@PathVariable("workId") Work work,
			@PathVariable("workType") WorkType workType,
			@RequestHeader("referer") String referer,
			ModelMap model
			) {
		service.changeSWorkType(work, workType);
		model.clear();	// Controller의 ModelAttribute가 저장하고 있는 필요없는 정보를 삭제한다.
		return "redirect:" + referer;
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  list  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/list/{state}")
	public String list(ModelMap model,
						HttpServletRequest request,
						@PathVariable("state") WorkState state,
						@RequestParam(value="today", required=false) boolean today,  
						@ModelAttribute WorkExample examples,	// 검색어 쿼리를 받아 Example객체를 만드는데 쓰인다.	
						@Board(size=10,orderBy="<stateTime") WorkBoard boardList		// 뷰에서 사용할 각종 데이터가 담긴다.
						) throws Exception {
		
		service.getList0( examples.setState(state), boardList );
		
		return "admin.work.list";
	}
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  list  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/list/test")
	public String list1() throws Exception {
		return "admin.work.list2";
	}
	
		
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  View ModelMap  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@Autowired WorkStateService stateService;
	
	@ModelAttribute("workTypes")
	public WorkType[] workTypes() {
		return WorkType.values();
	}
	@ModelAttribute("workStates")
	public List<WorkState> workStates() {
		return stateService.getAll();
	}
	
	@ExceptionHandler(NoSearchListException.class)
	public String noSearch(NoSearchListException ex) {
		return "admin.work.empty";
	}
		
}


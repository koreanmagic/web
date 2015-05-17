package kr.co.koreanmagic.web2.controller.work;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.service.WorkStateService;
import kr.co.koreanmagic.web2.controller.work.WorkController.WorkControllerMember;
import kr.co.koreanmagic.web2.servlet.exception.NoSearchListException;
import kr.co.koreanmagic.web2.support.argresolver.annotation.BoardList;
import kr.co.koreanmagic.web2.support.paging.GenericBoardList;

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
						@PathVariable("state") WorkState state,
						@RequestParam(value="today", required=false) boolean today,  
						@BoardList(size=10) GenericBoardList boardList
						) throws Exception {
		
		Object[] result = service.getStateList(state, today, boardList);
		
		// 데이터가 없을 경우
		if( (int)result[0] == 0 ) {
			if(today) return "redirect:/admin/work/list/" + state.getId();
			else return "redirect:/admin/work/list/" + state.getId() + 1;
		}
		
		boardList.setTotalSize((int)result[0]);
		boardList.setList((List<Work>)result[1]);
		return "admin.work.list";
	}
	
	// eq-같다  bt-사이값  nb-사이값제외  ge-이상  le-이하  gt-초과  lt-미만  ne-같지않다 
	// 거래처로 검색
	@RequestMapping(value="/list/{state}/{type}/{partnerId}")
	public String customerList(ModelMap model,
			@PathVariable("state") WorkState state,
			@PathVariable("partnerId") Partner partner,
			@PathVariable("type") String name,
			@BoardList(size=10) GenericBoardList boardList
			) throws Exception {
		
		Object[] result = service.getPartnerList(state, partner, name, boardList);
		boardList.setTotalSize((int)result[0]);
		boardList.setList((List<Work>)result[1]);
		return "admin.work.list";
	}
	
	// 거래처로 검색
	@RequestMapping(value="/test")
	public String test() throws Exception {
		return "admin.work.test";
	}
		
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  View ModelMap  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@Autowired WorkStateService stateService;
	
	@ModelAttribute("states")
	public List<Long> states() {
		return service.getStateCount();
	}
	@ModelAttribute("states_today")
	public List<Long> statesToday() {
		return service.getStateCountByDate();
	}

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


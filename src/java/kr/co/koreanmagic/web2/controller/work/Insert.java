package kr.co.koreanmagic.web2.controller.work;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.service.WorkStateService;
import kr.co.koreanmagic.web2.controller.work.WorkController.WorkControllerMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"command"})
public class Insert extends WorkControllerMember {

	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Insert  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/insert/{customer}", method=RequestMethod.GET)
	public String insertGet( ModelMap model, @PathVariable("customer") Customer customer ) throws Exception {
		Work work = service.getInitalBean();
		work.setWorkState(new WorkState(1));
		work.setCustomer(customer);
		model.put("command", work);
		return "admin.work.insert";
	}
	@RequestMapping(value="/insert/{customer}", method=RequestMethod.POST)
	public String insertPost(ModelMap model,
							@ModelAttribute("command") Work work,
							SessionStatus status) throws Exception {
		work.setStateTime(work.getInsertTime());
		service.add(work);
		status.isComplete();
		return "redirect:/admin/work/list/1";
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
	
}


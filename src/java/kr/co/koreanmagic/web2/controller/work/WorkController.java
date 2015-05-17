package kr.co.koreanmagic.web2.controller.work;

import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.service.WorkService;
import kr.co.koreanmagic.web2.controller.admin.AdminController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/work")
public class WorkController extends AdminController<Work, String> {

	protected Logger logger = Logger.getLogger(getClass());
	protected @Autowired WorkService service;
	
	@Override
	public WorkService getService() {
		return this.service;
	}	
	@Override
	public String name() { return "work"; }
	
	
	
	@RequestMapping("/admin/work")
	static class WorkControllerMember {
		protected Logger logger = Logger.getLogger(getClass());
		@Autowired protected WorkService service;
	}

	
}


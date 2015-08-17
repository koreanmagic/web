package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.service.ManagerService;
import kr.co.koreanmagic.web2.controller.admin.PartnerController.PartnerMemberController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/manager")
@SessionAttributes({"command"})
public class ManagerController extends PartnerMemberController<Manager> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired ManagerService service;

	@Override
	public ManagerService getService() {
		return this.service;
	}	
	@Override
	public String name() { return "manager"; }
	
	
}

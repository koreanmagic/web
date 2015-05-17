package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.service.BankService;
import kr.co.koreanmagic.web2.controller.admin.PartnerController.PartnerMemberController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/bank")
@SessionAttributes({"command"})
public class BankController extends PartnerMemberController<Bank> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired BankService service;

	@Override
	public BankService getService() {
		return this.service;
	}
	@Override
	public String name() { return "bank"; }	
	
}

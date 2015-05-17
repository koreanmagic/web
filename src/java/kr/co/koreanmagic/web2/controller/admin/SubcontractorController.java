package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.service.SubcontractorService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/subcontractor")
public class SubcontractorController extends PartnerController<Subcontractor> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired SubcontractorService service;

	@Override
	public SubcontractorService getService() {
		return this.service;
	}
	@Override
	public String name() { return "subcontractor"; }
	
	
	@RequestMapping("list/all")
	@ResponseBody
	public Object getAll() {
		return service.getAll();
	}
	
}

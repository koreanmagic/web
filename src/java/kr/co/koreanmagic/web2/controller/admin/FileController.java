package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkFile;
import kr.co.koreanmagic.service.WorkFileService;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/file")
public class FileController extends AdminController<WorkFile, Long> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired WorkFileService service;

	@Override
	public WorkFileService getService() {
		return this.service;
	}	
	@Override
	public String name() { return "file"; }
	
}

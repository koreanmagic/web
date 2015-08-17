package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkFile;
import kr.co.koreanmagic.service.WorkFileService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//@Controller
//@RequestMapping("/admin/file")
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

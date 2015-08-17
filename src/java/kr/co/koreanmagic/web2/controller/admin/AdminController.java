package kr.co.koreanmagic.web2.controller.admin;

import java.io.Serializable;

import kr.co.koreanmagic.commons.StringUtils;
import kr.co.koreanmagic.service.GenericService;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/*
 * 어드민 페이지의 최상위 컨트롤러
 * 
 * PageInfo 각 컨트롤러가 여는 페이지마다 들어가는 고정정보다.
 * 각 페이지를 구분할 이름 등이 들어간다.
 * 
 */
public abstract class AdminController<T, P extends Serializable> {
	
	private Logger logger = Logger.getLogger(getClass());
	
	protected abstract GenericService<T, P> getService();
	protected abstract String name();
	
	// *********** [get] GET by ID *********** // 
	@RequestMapping(value="/test/{name}", method=RequestMethod.GET)
	public String test( @PathVariable("name") String name ) {
		return "tiles3.admin.test." + name;
	}

	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Ajax  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	// *********** [get] GET by ID *********** // 
	@RequestMapping(value="/ajax/get/id/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Object ajaxGet( @PathVariable("id") P id ) {
		return getService().get(id, true);
	}
	
	
	
	// *********** [Modify] Get Bean for Modify (by ID) *********** // 
	@RequestMapping(value="/ajax/modify/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Object ajaxModify( @PathVariable("id") P id,
								ModelMap model
							){
		T bean = getService().get(id, true);
		model.put("command", bean);		// 빈은 세션에 저장
		return bean;					// 값은 내보낸다.
	}
	
	// *********** [Modify] Get Bean for Modify (by ID) *********** // 
	@RequestMapping(value="/ajax/modify", method=RequestMethod.POST)
	@ResponseBody
	public void ajaxModifyPost( @ModelAttribute("command") T bean,
									SessionStatus status
									){
		getService().update(bean);
		status.isComplete();
	}
	
	
	// *********** [Modify] Get Bean for Modify (by ID) *********** // 
	@RequestMapping(value="/ajax/delete/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void ajaxDelete( @PathVariable("id") T bean ){
		logger.debug(bean.getClass());
		getService().delete(bean);
	}
	
	// 수정없이 취소할 경우, 세션을 비운다.
	@RequestMapping(value="/ajax/modify/done", method=RequestMethod.GET)
	@ResponseBody
	public void modifyDone( SessionStatus session ) throws Exception {
		session.isComplete();
	}
	
	protected void log(Object...t) {
		System.out.println(StringUtils.join("", ",", t));
	}
}

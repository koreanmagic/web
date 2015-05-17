package kr.co.koreanmagic.web2.controller.work;

import java.util.List;
import java.util.Map;

import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkResourceFile;
import kr.co.koreanmagic.service.WorkResourceFileService;
import kr.co.koreanmagic.web2.controller.work.WorkController.WorkControllerMember;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes({"bean", "command"})
public class Editor extends WorkControllerMember {

	
	// *********** [get] GET by ID *********** // 
	@RequestMapping(value="/editor/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Object ajaxGet( @PathVariable("id") String id ) {
		return service.getWork(id);
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  [Ajax] Editor 수정  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	// 워크리스트의 에디터를 위한 세션 저장 
	// 수정 에디터를 띄우면서 이 핸들러를 실행한다.
	@RequestMapping(value="/editor/init/{id}")
	@ResponseBody
	public void sessionSave(@PathVariable("id") Work work, ModelMap model) {
		model.put("bean", work);
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  작업정보 수정  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	// 작업 정보 데이터를 가지고 온다.
	@RequestMapping(value="/editor/data/", method=RequestMethod.GET)
	@ResponseBody
	public Object dataGet(ModelMap model, @ModelAttribute("bean") Work bean ) throws Exception {
		Work work = service.get(bean.getId(), true);
		model.put("command", work);
		return work;
	}
	
	@RequestMapping(value="/editor/data/", method=RequestMethod.POST)
	@ResponseBody
	public void dataPost( @ModelAttribute("command") Work work ) throws Exception {
		service.update(work);
	}
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  associate 객체 수정  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/editor/values", method=RequestMethod.GET)
	@ResponseBody
	public Object associateGet( ModelMap model,
									@RequestParam("keys") String[] keys,
									@ModelAttribute("bean") Work bean
								) throws Exception {
		Object result = service.scalaMapById( bean.getId(), keys);
		return result;
	}
	
	@RequestMapping(value="/editor/{type}/{changeId}", method=RequestMethod.GET)
	@ResponseBody
	public void associateUpdate( @ModelAttribute("bean") Work bean,
									@PathVariable("type") String type,
									@PathVariable("changeId") Object targetId
								) throws Exception {
		service.updateMember(bean, type, targetId);
	}
	
	// 방문/직접배달 --> 택배(+ 주소)
	@RequestMapping(value="/editor/delivery", method=RequestMethod.POST)
	@ResponseBody
	public void addressUpdate( @ModelAttribute("bean") Work bean,
								@RequestParam Map<String, Object> valueMap
							) throws Exception {
		service.updateDelivery(bean, valueMap);
	}
	
	@RequestMapping(value="/editor/test", method=RequestMethod.GET)
	@ResponseBody
	public Object test(@RequestParam Map<String, Object> valueMap) {
		logger.debug(valueMap);
		return valueMap;
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Delete  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/delete/{workId}", method=RequestMethod.GET)
	@ResponseBody
	public String insertGet(ModelMap model, @PathVariable("workId") Work work
							) throws Exception {
		service.delete(work);
		return "{\"success\": \"true\"}";
	}
	
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  File Upload  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/editor/resource/upload", method=RequestMethod.POST)
	@ResponseBody
	public void uploadResource(ModelMap model,
							@ModelAttribute("bean") Work work,
							@RequestParam("file") List<MultipartFile> files,
							@Service WorkResourceFileService service
							) throws Exception {
		for(MultipartFile file : files) {
			logger.debug(file.getOriginalFilename());
		}
		service.saveFile(files, work);
	}
	
	@RequestMapping(value="/editor/resource/list", method=RequestMethod.GET)
	@ResponseBody
	public Object getResources( @ModelAttribute("bean") Work work,
								@Service WorkResourceFileService resourceService ) throws Exception {
		return resourceService.getList(work);
	}
	
	@RequestMapping(value="/editor/resource/delete/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void removeResource( @PathVariable("id") WorkResourceFile resource,
									@Service WorkResourceFileService resourceService ) throws Exception {
		resourceService.delete(resource);
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  File Resource  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/get/resource/size/{workId}", method=RequestMethod.GET)
	@ResponseBody
	public String resourceCount(@PathVariable("workId") Work work,
								@Service WorkResourceFileService service
							) throws Exception {
		int size = service.getSize(work);
		return "{\"size\": \"" + size + "\"}";
	}	
}


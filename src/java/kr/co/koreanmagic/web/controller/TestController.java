package kr.co.koreanmagic.web.controller;

import java.util.List;

import kr.co.koreanmagic.web.domain.Work;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/")
public class TestController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping(value="test", method=RequestMethod.GET)
	public String test2() {
		logger.debug("get");
		return "default/worklist/test";
	}
	
	@RequestMapping(value="test", method=RequestMethod.POST)
	public String test(@RequestParam(value="file", required=false) List<MultipartFile> resources) {
		logger.debug("post");
		int size = resources.size();
		logger.debug("파일 갯 수 : " + size);
		if(size > 0) {
			for(MultipartFile file : resources)
				logger.debug(file.getOriginalFilename() + " (" + file.getSize() + " Bytes)");
		}
		return "default/worklist/test";
	}
	
	

}

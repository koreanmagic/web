package kr.co.koreanmagic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class JQueryPlugInDeveloper {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping("jquery-fn/{name}")
	public String toes(@PathVariable("name") String name) {
		logger.debug("와써ㅃ");
		return "jquery-fn/" + name;
	}

}

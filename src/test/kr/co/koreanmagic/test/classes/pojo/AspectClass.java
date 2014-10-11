package kr.co.koreanmagic.test.classes.pojo;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AspectClass {
	
	Logger logger = Logger.getLogger("test");
	
	public String say(String name) {
		logger.debug("음히히");
		return "안녕하세요! " + name + "님";
	}

}

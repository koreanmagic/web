package kr.co.koreanmagic.web.servlet.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import kr.co.koreanmagic.web.util.WebDatePrint;

import org.apache.log4j.Logger;

public class TraceTags extends GeneralTagSupport {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		
		HttpServletRequest req = getRequest();
		
		logger.debug("\n\n\n---------------------- ▼ Koreanmagic Taglib Util ▼  ----------------------\n");
		for(String l : WebDatePrint.printRequestAttribute(req))
			logger.debug(l);
		logger.debug("\n----------------------\n\n\n");
			
	}

}

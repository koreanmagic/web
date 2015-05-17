package kr.co.koreanmagic.web2.servlet.tag;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Trace extends SimpleTagSupport {
	
	
	@Override
	public void doTag() throws JspException, IOException {
		
		HttpServletRequest req = (HttpServletRequest)((PageContext)getJspContext()).getRequest();
		Enumeration<?> names = req.getAttributeNames();
		
		String name = null;
		System.out.println("--------------------------- Request Attribute ---------------------------");
		while(names.hasMoreElements()) {
			name = names.nextElement().toString();
			System.out.println(name + ">  " + req.getAttribute(name));
		}
		System.out.println("\n\n\n");
		
		System.out.println("--------------------------- Request Parameters ---------------------------");
		names = req.getParameterNames();
		while(names.hasMoreElements()) {
			name = names.nextElement().toString();
			System.out.println(name + ">  " + req.getParameter(name));
		}
		
	}

}

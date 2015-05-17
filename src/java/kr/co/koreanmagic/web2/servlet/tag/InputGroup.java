package kr.co.koreanmagic.web2.servlet.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
public class InputGroup extends SimpleTagSupport {
	
	private String commandName;		// request에 저장된 valueMap의 키
	private String[] props;			// 프로퍼티 네임즈
	private String[] size;			// 사이즈 배열
	private String[] before;		// 인풋 앞에 붙는 
	private String sepatator;		// 인풋 사이
	
	@Override
	public void doTag() throws JspException, IOException {
		
		// request에서 값을 꺼내온다.
		HttpServletRequest req = (HttpServletRequest)((PageContext)getJspContext()).getRequest();
		
		Map<String, Object> map = (Map<String, Object>)req.getAttribute( commandName == null ? "command" : commandName );
		
	}
	

}

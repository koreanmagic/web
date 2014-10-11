package kr.co.koreanmagic.web.servlet.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class GeneralTagSupport extends SimpleTagSupport {
	
	private HttpServletResponse res;
	private HttpServletRequest req;
	
	private HttpSession session;
	private PageContext context;
	
	private JspWriter writer;
	
	public GeneralTagSupport() {
		super();
	}
	
	/* HttpServletRequest 반환 */
	protected HttpServletResponse getResponse() {
		if(this.res == null)
			this.res = (HttpServletResponse)getPageContext().getResponse();
		return this.res;
	}
	/* HttpServletResponse 반환 */
	protected HttpServletRequest getRequest() {
		if(this.req == null)
			this.req = (HttpServletRequest)getPageContext().getRequest();
		return this.req;
	}
	/* HttpSession 반환 */
	protected HttpSession getSession() {
		if(this.session == null)
			this.session = getPageContext().getSession();
		return this.session;
	}
	
	/* PageContext 반환 */
	protected PageContext getPageContext() {
		if(this.context == null)
			this.context = (PageContext)getJspContext();
		return this.context;
	}
	
	/* JspWriter 반환 */
	protected JspWriter getOut() {
		if(this.writer == null)
			this.writer = getPageContext().getOut();
		return this.writer;
	}
	
	/* Jsp에 직접 쓰기 */
	protected void out(Object obj) throws IOException {
		getOut().println(obj);
	}
	
	/* PageContext 어트리뷰트 읽고 쓰기 */
	protected void setAttribute(String key, Object value) {
		getPageContext().setAttribute(key, value);
	}
	private<T> T getAttribute(String key) {
		return (T)getPageContext().getAttribute(key);
	}
	
	/* 부모태그 가지고 오기 
	public<T> T getParentTag() throws JspException {
		JspTag jspTag = super.getParent();
		
		if(jspTag == null || !getClass().isAssignableFrom(jspTag.getClass()))
			throw new JspException("잘못된 부모 커스텀 액션입니다.");
		
		return (T)jspTag;
	}*/
	

}

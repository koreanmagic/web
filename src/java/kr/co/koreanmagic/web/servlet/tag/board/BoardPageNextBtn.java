package kr.co.koreanmagic.web.servlet.tag.board;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;

import kr.co.koreanmagic.web.servlet.tag.GeneralTagSupport;


public class BoardPageNextBtn extends GeneralTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		
		JspTag jspTag = getParent();
		if(jspTag == null || !(jspTag instanceof BoardPageTag))
			throw new JspException("잘못된 부모 커스텀 액션입니다.");
		
		BoardPageTag parent = (BoardPageTag)jspTag;
		
		if(parent.getPos() == BoardPageTag.NEXT_BTN)
			getJspBody().invoke(null);
		
	}

}

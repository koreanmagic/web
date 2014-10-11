package kr.co.koreanmagic.web.controller.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

public class WorkSizeEditor extends PropertyEditorSupport {

	Logger logger = Logger.getLogger(getClass());
	
	// setAsText를 통해 값을 넣어준 후, getValue()로 가져간다.
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String result = "";
		if(text != null && text.length() > 2)
			result = text.replaceAll(",", "-"); 
		setValue(result);
	}
	
	
	@Override
	public String getAsText() {
		return null;
	}
}

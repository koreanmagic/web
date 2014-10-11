package kr.co.koreanmagic.web.controller.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.web.domain.WorkDraft;
import kr.co.koreanmagic.web.domain.WorkFile;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.service.WorkDraftService;
import kr.co.koreanmagic.web.service.WorkFileService;
import kr.co.koreanmagic.web.service.WorkResourceService;
import kr.co.koreanmagic.web.service.WorkService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkResourceInterceptor extends HandlerInterceptorImpl {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired private WorkService workService;
	@Autowired private WorkFileService workFileService;
	@Autowired private WorkDraftService workDraftService;
	@Autowired private WorkResourceService workResourceService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String query = request.getQueryString();
		logger.debug(query);
		if(query.lastIndexOf("id=") != -1) { // id 쿼리가 있다는 얘기는 파일을 직접적으로 요청한다는 뜻이다. 파일 데이터베이스를 업데이트 해준다.
			String id = getId(query);
			String fileType = getFileType(query);
			count(fileType, Integer.valueOf(id)); // 업데이트
		}
		
		return true;
	}
	
	private void count(String fileType, Integer id) {
		if(fileType.equals(WorkFile.class.getSimpleName()))
			this.workFileService.addCount(id);
		if(fileType.equals(WorkDraft.class.getSimpleName()))
			this.workDraftService.addCount(id);
		if(fileType.equals(WorkResource.class.getSimpleName()))
			this.workResourceService.addCount(id);
	}
	
	private static final String ALL_STRING = "([a-zA-z가-힣0-9]+)";
	private static final Pattern FILE_TYPE = Pattern.compile("fileType=" + ALL_STRING);
	private static final Pattern ID = Pattern.compile("id=" + ALL_STRING);
	
	private String getFileType(String query) {
		return matcher(FILE_TYPE.matcher(query));
	}
	private String getId(String query) {
		return matcher(ID.matcher(query));
	}
	
	private String matcher(Matcher m) {
		if(m.find())
			return m.group(1);
		else
			return null;
	}


	@Override
	public String[] getPathPatterns() {
		return new String[]{"/work/**/*"};
	}

}

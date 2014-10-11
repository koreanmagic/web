package kr.co.koreanmagic.web.controller.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kr.co.koreanmagic.commons.KoCollectionView;
import kr.co.koreanmagic.web.controller.support.webdata.CreateParamValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;


public class CreateParamValue_TEST {

	Logger logger = Logger.getLogger("test");
	
	
	List<String> query = new ArrayList<>();
	
	/*
	 * HttpServletRequest의 paramValue와 paramValues를 흉내낸 클래스
	 * 무작위로 들어오는 key, value값을 분류해 저장한다.
	 * 중복된 값이 있으면 Map<String, String[]>에 저장되고
	 * 값이 하나면 Map<String, String>에 저장된다.
	 */
	@Test
	public void test() {
		Map<String, String> paramValue= null;
		Map<String, String[]> paramValues = null;
		
		CreateParamValue params = new CreateParamValue();
		
		query.add("name=고");
		query.add("name=정");
		query.add("file=txt");
		query.add("name=철");
		query.add("old=30");
		query.add("file=name");
		
		puts(params, query);
		
		paramValue = params.getParamValue();
		paramValues = params.getParamValues();
		
		assertThat(paramValue.toString(), is("{old=30}"));
		assertThat(KoCollectionView.mapToString(paramValues), is("{file=txt,name&name=고,정,철}"));
		
	}
	
	private String arrayMapJoin(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder("{");
		Set<Entry<String, String[]>> entrys = map.entrySet();
		for(Entry<String, String[]> entry : entrys) {
			sb.append(entry.getKey()).append("=").append( StringUtils.join(entry.getValue(), ",")).append("&");
		}
		return sb.delete(sb.length()-1, sb.length()).append("}").toString();
	}
	
	private void puts(CreateParamValue params, List<String> list) {
		String[] ss = null;
		for(String l : list) {
			ss = l.split("=");
			params.put(ss[0], ss[1]);
		}
	}
	
	

}

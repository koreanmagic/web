package kr.co.koreanmagic.web.domain;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class Work테스트 {
	
	Logger logger = Logger.getLogger("test");

	private Work work;
	
	@Test
	public void dd() {
		String s = "2014-01-01";
		
		logger.debug(ts);
	}
	
	@Before
	public void init() {
		Work work = new Work();
		work.setId("140420001");
		work.setInsertTime(new Timestamp(new Date().getTime()));
		work.setCondition(null);
		//work.setCustomer("한컴기획");
		//work.setItem("전단");
		work.setItemType("누보지");
		work.setCount(1);
		//work.setNumber(4000);
		//work.setSize("210-297");
		work.setUnit("mm");
		work.setBleed(2);
		work.setMemo("최고의 디자인");
		work.setTag("멋쟁이");
		work.setFilepath("c:");
		work.setFiletype("pdf");
		
		this.work = work;
	}
	
	@Test
	public void Work파일명() {
		logger.debug(work);
	}
	
	

}

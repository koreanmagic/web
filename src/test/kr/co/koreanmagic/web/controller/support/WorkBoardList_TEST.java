package kr.co.koreanmagic.web.controller.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.support.WorkBoardList;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class WorkBoardList_TEST {

	Logger logger = Logger.getLogger("test");
	private Path path = Paths.get("G:/작업공유-사본/test");
	
	private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Test
	public void 썸네일주소() {
		WorkBoardList list = new WorkBoardList();
		list.setRootPath(path);
		/*
		 * InserDate는 폴더 구조가 되고, Id는 썸네일 파일명이 된다.
		 */
		Work work = getWork("140501001", "2014-05-01"); 
		Path path = list.getImg(work);
		assertThat(path.toString(), is("2014-05\\01\\imgs\\140501001.jpg"));
	}
	
	
	private Work getWork(String id, String date) {
		Work work = new Work();
		work.setId(id);
		work.setInsertTime(new Timestamp(new DateTime(date).getMillis()));
		return work;
	}

}

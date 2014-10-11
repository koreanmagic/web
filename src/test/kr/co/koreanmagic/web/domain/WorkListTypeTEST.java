package kr.co.koreanmagic.web.domain;

import kr.co.koreanmagic.web.db.mybatis.support.WorkListTypes;

import org.apache.log4j.Logger;
import org.junit.Test;

public class WorkListTypeTEST {

	Logger logger = Logger.getLogger("test");
	
	@Test
	public void test() {
		WorkListTypes type = WorkListTypes.valueOf(WorkListTypes.class, "NORMAL");
		logger.debug(WorkListTypes.valueOf("NORMAL"));
	}

}

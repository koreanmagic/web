package kr.co.koreanmagic.db.mybatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.sql.DataSource;

import kr.co.koreanmagic.web.db.mybatis.config.ConfigureMyBatis;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkConditionMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkDraftMapper;
import kr.co.koreanmagic.web.domain.WorkCondition;
import kr.co.koreanmagic.web.domain.WorkResource;
import kr.co.koreanmagic.web.service.WorkDraftService;
import kr.co.koreanmagic.web.service.WorkFileService;
import kr.co.koreanmagic.web.service.WorkResourceService;
import kr.co.koreanmagic.web.service.WorkService;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/*
 * 스프링-마이바티스 연동 설정을 통해 마이바티스를 테스트한다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ConfigureMyBatis.class})
public class 마이바티스_테스트 {

	/*@Autowired DataSource dataSource;
//	@Autowired WorkService workService;
//	@Autowired WorkFileService workFileService;
//	@Autowired WorkDraftService workDraftService;
//	@Autowired WorkResourceService workResourceService;
//	@Autowired WorkDraftMapper workDraftMapper;
	@Autowired WorkConditionMapper workCondifionMapper;
	
	Logger logger = Logger.getLogger("test");
	
	
	@Test
	public void 타입핸들러() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		Stream<String> stream = list.stream();
		stream.forEach(x -> logger.debug(x));
		logger.debug(System.getProperty("java.version"));
		logger.debug("울랄라");
	}
	
	
	//@Test
	public void 커스텀SQL() {
		Map<String, String> map = new HashMap<>();
		map.put("value", "condition=1");
		List<Work> list = workService.getListByCustomSQL("con_id=5", 0, 5);
		
		logger.debug("사이즈 : " + list.size());
		logger.debug(workService.getCountByCustomSQL("con_id=3"));
	}
	
	
	@Test
	public void 커넥션() throws Exception {
		Connection c = dataSource.getConnection();
		Statement stmt = c.createStatement();
		ResultSet result = stmt.executeQuery("SELECT '얍출바레' asdf, first_name, middle_name, last_name FROM test WHERE id = 1");
		logger.debug(result.next());
		ResultSetMetaData meta = result.getMetaData();
		logger.debug(meta.getColumnLabel(1));
		logger.debug(meta.getColumnName(1));
	}
	
	
	public WorkResource getResource(String fileName, String fileType, String id) {
		WorkResource r = new WorkResource();
		r.setFileName(fileName);
		r.setFileType(fileType);
		r.setWorkId(id);
		r.setIsUse(false);
		r.setParentPath("");
		r.setUploadTime(new Timestamp(new Date().getTime()));
		return r;
	}
	
	
	
	private String change(String str) {
		return str.replace("\\", "/");
	}
	
	private WorkCondition getCondition(int id) {
		return getCondition(id, null);
	}
	private WorkCondition getCondition(int id, String name) {
		WorkCondition condition = new WorkCondition();
		condition.setId(id);
		condition.setName(name);
		return condition;
	}
	
	private void printList(List<?> list) {
		for(Object obj : list)
			logger.debug(obj);
	}*/
	
}

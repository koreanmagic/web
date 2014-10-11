package kr.co.koreanmagic.db.mybatis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkConditionMapper;
import kr.co.koreanmagic.web.domain.WorkCondition;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

public class 마이바티스_빌더 {

	
	/*final String PATH = "/kr/co/koreanmagic/web/db/mybatis/mapper/MySiteMapper.xml";
	
	@Test
	public void init() throws Exception {
		String resource = "mybatis-config.xml";
		InputStream is;
		
		is = Resources.getResourceAsStream(resource);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null) {
		}

		
		is = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
		
		SqlSession session = sqlSessionFactory.openSession();
		List<WorkCondition> conditions = session.getMapper(WorkConditionMapper.class).getAll();
	}
	
	@Test
	public void test() {
		SqlSessionFactory c;
		
	}
	
	private static void out(Object obj) {
		System.out.println(obj);
	}*/

}

package kr.co.koreanmagic.hibernate3.DB이전;

import java.util.ArrayList;
import java.util.List;

import kr.co.koreanmagic.hibernate3.DB이전.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class 작업리스트 extends TestManager {

	@Autowired SessionFactory factory;
	
	public static String workId;
	
	@Test
	@Transactional
	@Rollback(false)
	public void test() {
		
		Session session = factory.openSession();
		List<Work> works = new ArrayList();
		works = session.createSQLQuery("SELECT id, tag, memo FROM work_list2")
													.setResultTransformer(Transformers.aliasToBean(Work.class))
													.list();
		int i = 1, l=0;
		WorkState state = null;
		kr.co.koreanmagic.hibernate3.mapper.domain.Work w = null;
		for(Work work : works) {
			
			w = new kr.co.koreanmagic.hibernate3.mapper.domain.Work();
			w = ws.get(work.getId());
			
			w.setMemo(work.getTag() + "\n" + work.getMemo());
			
			ws.update(w);
			ws.flush();
		}
		
		
		
		
	}

}

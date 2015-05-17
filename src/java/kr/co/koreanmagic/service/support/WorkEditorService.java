package kr.co.koreanmagic.service.support;

import java.util.Map;

import javax.annotation.PostConstruct;

import kr.co.koreanmagic.hibernate3.mapper.domain.Work;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Component
public class WorkEditorService {
	
	@Autowired SessionFactory factory;
	private Session session;
	
	
	@PostConstruct
	public void init() {
		session = factory.openSession();
		session.setFlushMode(FlushMode.MANUAL);
		
		getSession();
		done();
	}
	
	public Work getWork(String id) {
		Work work = (Work)getSession().get(Work.class, id);
		done();
		return work;
	}
	
	private Session getSession() {
		return ManagedSessionContext.bind(session);
	}
	
	private void done() {
		session = ManagedSessionContext.unbind(factory);
	}
	
	public void complete() {
		session.flush();
		session.beginTransaction().commit();
		session.close();
	}
	
	@Transactional(readOnly=true)
	public void init(String workId) {
		Map<String, Object> map = (Map<String, Object>)factory.getCurrentSession()
							.createQuery("SELECT customer.id as c, subcontractor.id as s, manager.id as m, address.id as a FROM Work w WHERE w.id = :id")
							.setParameter("id", workId)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		Object v = null;
		for(String key : new String[]{"c, s, m, a"}) {
			v = map.get(key);
			if(v != null) {
				
			}
		}
		
	}
	
}

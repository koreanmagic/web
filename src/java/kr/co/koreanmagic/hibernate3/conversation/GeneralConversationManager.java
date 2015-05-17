package kr.co.koreanmagic.hibernate3.conversation;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * 컨버세이션 Session 확장의 기본 구현
 */
public class GeneralConversationManager implements ConversationManager {

	SessionFactory factory;
	private Session session;
	private boolean readOnly;
	
	
	public GeneralConversationManager(SessionFactory factory) {
		this(factory, false);
	}
	public GeneralConversationManager(SessionFactory factory, boolean readOnly) {
		this.factory = factory;
		this.readOnly = readOnly;
	}
	
	
	@Override
	public Session complate() {
		if(!readOnly) getBindSession().flush();
		return unbind();
	}

	@Override
	public Session getBindSession() {
		if(session == null) {
			session = factory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
		}
		return ManagedSessionContext.bind(session);
	}
	@Override
	public Session unbind() {
		return ManagedSessionContext.unbind(factory);
	}
	
	
	

}

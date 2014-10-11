package kr.co.koreanmagic.hibernate3;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class HibernateTestDao {
	
	private Session session;
	private Transaction transaction;
	@Autowired(required=false) SessionFactory sessionFactory;
	@Autowired(required=false) PlatformTransactionManager transactionManager;
	
	
	protected void setFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	protected SessionFactory sessionFactory() {
		return this.sessionFactory;
	}
	
	protected PlatformTransactionManager transactionManager() {
		return this.transactionManager;
	}
	
	protected<T> T cast(Object obj) {
		return (T)obj;
	}
	
	@SuppressWarnings("unchecked")
	protected<T> T get(Class<T> t, Serializable primaryKey) {
		return (T)session().get(t, primaryKey);
	}
	@SuppressWarnings("unchecked")
	protected<T> T load(Class<T> t, Serializable primaryKey) {
		return (T)session().load(t, primaryKey);
	}
	@SuppressWarnings("unchecked")
	protected<T> T merge(T t) {
		return (T)session().merge(t);
	}
	@SuppressWarnings("unchecked")
	protected Serializable save(Object persist) {
		return session().save(persist);
	}
	@SuppressWarnings("unchecked")
	protected void update(Object persist) {
		session().update(persist);
	}

	
	protected Session currentSession() {
		return session = sessionFactory.getCurrentSession();
	}
	
	
	protected void rollback() {
		if(transaction != null)
			transaction.rollback();
	}
	
	protected void commit() {
		if(transaction != null)
			transaction.commit();
	}
	
	protected Transaction transaction() {
		if(session == null) session();
		this.transaction = session.beginTransaction();
		return this.transaction;
	}
	
	protected Session session() {
		return session(true);
	}
	
	protected Session session(boolean current) {
		
		if(!current)
			return session = sessionFactory.openSession();
		
		if(session == null)
			return session = sessionFactory.openSession();
		
		return session;
	}
	
	protected Session close() {

		if (session == null)
			throw new RuntimeException("Session이 없습니다.");

		Session closed = session;
		session.close();
		session = null;

		return closed;
	}
	
	protected Object log(Object obj) {
		System.out.println("----------------------------------------------------------------------");
		System.out.println(obj);
		return obj;
	}
	
	

	/*
	 *  == 비교
	 */
	protected void same(Object o1, Object o2) {
		String s1 = o1.getClass().getSimpleName();
		String s2 = o2.getClass().getSimpleName();
		
		log(s1 + " == " + s2 + " ==>  " + booleanToString(s1 == s2));
	}
	
	/*
	 * equals 비교
	 */
	protected void equals(Object o1, Object o2) {
		String s1 = o1.getClass().getSimpleName();
		String s2 = o2.getClass().getSimpleName();
		
		log(s1 + " equals( " + s2 + " ) ==>  " + booleanToString(s1.equals(s2)));
	}
	
	private String booleanToString(boolean b) {
		return b ? "같다" : "다르다";
	}
	
	
}

package kr.co.koreanmagic.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	private static Configuration config;
	
	static {
		try {
			config = new Configuration().configure();
			sessionFactory = config.buildSessionFactory();
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Configuration getConfiguration() {
		return config;
	}
	
	public static void shutdown() {
		getSessionFactory().close();
	}
	
}

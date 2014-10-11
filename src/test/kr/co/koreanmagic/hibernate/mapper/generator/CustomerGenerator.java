package kr.co.koreanmagic.hibernate.mapper.generator;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public abstract class CustomerGenerator implements IdentifierGenerator {

	@Override
	public final Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		
		return generate(session, session.connection(), object);
	}
	
	abstract Serializable generate(SessionImplementor session, Connection con, Object object)
			throws HibernateException;
	
}

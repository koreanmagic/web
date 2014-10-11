package kr.co.koreanmagic.hibernate.mapper.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.koreanmagic.hibernate.mapper.domain.WorkGroup;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomerSequence implements IdentifierGenerator {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		
		WorkGroup workGroup = (WorkGroup)object;
		if(workGroup.getId() != null)
			return workGroup.getId();
		
		Connection con = session.connection();
		PreparedStatement ps = null;
		try {
			
			ps = con.prepareStatement("SELECT test_date_seq(curdate())");
			ResultSet rs = ps.executeQuery();
			rs.next();
			return log(rs.getString(1));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	

	private String log(Object msg) {
		log0(msg);
		return msg.toString();
	}
	
	private void log0(Object msg) {
		System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ >" + msg);
	}
}

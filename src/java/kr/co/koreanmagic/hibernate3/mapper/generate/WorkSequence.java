package kr.co.koreanmagic.hibernate3.mapper.generate;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import kr.co.koreanmagic.hibernate3.mapper.domain.Work;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class WorkSequence implements IdentifierGenerator {

	private static Logger logger = Logger.getLogger(WorkSequence.class);
	
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		
		String key = null;
		
		Connection con = session.connection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SELECT date_seq(curdate())");
			ResultSet rs = ps.executeQuery();
			rs.next();
			key = rs.getString(1);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return key;
	}	

}

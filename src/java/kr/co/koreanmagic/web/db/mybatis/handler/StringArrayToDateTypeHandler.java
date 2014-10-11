package kr.co.koreanmagic.web.db.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;

public class StringArrayToDateTypeHandler implements TypeHandler<String[]>  {

	Logger logger = Logger.getLogger(getClass());

	@Override
	public String[] getResult(ResultSet arg0, String arg1) throws SQLException {
		return null;
	}

	@Override
	public String[] getResult(ResultSet arg0, int arg1) throws SQLException {
		return null;
	}

	@Override
	public String[] getResult(CallableStatement arg0, int arg1) throws SQLException {
		return null;
	}

	/* DTO�� ����ִ� ���� ��ȯ���Ѽ� �����ͺ��̽��� ����ϴ� �޼��� */
	@Override
	public void setParameter(PreparedStatement ps, int columnIdx, String[] type, JdbcType jdbcType) throws SQLException {
		try {
			ps.setString(columnIdx, StringUtils.join(type));
		} catch (SQLException e){
			logger.fatal(e);
		}
	}

}

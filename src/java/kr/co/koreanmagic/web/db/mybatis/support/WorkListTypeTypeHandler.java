package kr.co.koreanmagic.web.db.mybatis.support;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

public class WorkListTypeTypeHandler implements TypeHandler<WorkListTypes> {

	
	Logger logger = Logger.getLogger(getClass());
	
	// 입력하는 SQL에서의 값 컨버팅
	@Override
	public void setParameter(PreparedStatement ps, int i, WorkListTypes parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getName());
		
	}

	
	// 각 세가지 방법을 통해 값을 가져올 수 있다.
	@Override
	public WorkListTypes getResult(ResultSet rs, String columnName) throws SQLException {
		logger.debug("getResult(ResultSet rs, String columnName)");
		String result = rs.getString(columnName);
		return WorkListTypes.parseOf(result);
	}

	@Override
	public WorkListTypes getResult(ResultSet rs, int columnIndex) throws SQLException {
		logger.debug("getResult(ResultSet rs, int columnIndex)");
		String result = rs.getString(columnIndex);
		return WorkListTypes.parseOf(result);
	}

	@Override
	public WorkListTypes getResult(CallableStatement cs, int columnIndex) throws SQLException {
		logger.debug("getResult(CallableStatement cs, int columnIndex)");
		String result = cs.getString(columnIndex);
		return WorkListTypes.parseOf(result);
	}

}

package kr.co.koreanmagic.hibernate3.mapper.works;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import kr.co.koreanmagic.hibernate3.work.NativeWork;

/*
 * 몇가지 리스트의 
 */
public class WorkStatusCounts implements NativeWork<Map<String, Long>> {


	private static final String[] PARAMETERS = {"biz_num", "work_num"};
	

	@Override
	public Map<String, Long> execute(Connection connection) throws SQLException {
		
		CallableStatement call = connection.prepareCall("call test_call2(?,?)");
		
		for(String param : PARAMETERS)
			call.registerOutParameter(param, Types.BIGINT);
		
		call.execute();
		
		Map<String, Long> result = new HashMap<>(PARAMETERS.length);
		
		for(String param : PARAMETERS)
			result.put(param, call.getLong(param));
		
		return result;
	}
	

}

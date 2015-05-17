package kr.co.koreanmagic.hibernate3.mapper.works;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.koreanmagic.hibernate3.work.NativeWork;

/*
 * 몇가지 리스트의 
 */
public class WorkStatusCounts implements NativeWork<List<Integer>> {


	private static final String[] PARAMETERS = {"state1", "state2", "state3", "state4", "state5", "state6", "state7"};
	

	@Override
	public List<Integer> execute(Connection connection) throws SQLException {
		
		CallableStatement call = connection.prepareCall("call work_list_stats(?,?,?,?,?,?,?)");
		for(String param : PARAMETERS)
			call.registerOutParameter(param, Types.BIGINT);
		
		call.execute();
		
		//Map<String, Long> result = new HashMap<>(PARAMETERS.length);
		List<Integer> result = new ArrayList<>();
		
		for(String param : PARAMETERS)
			result.add(call.getInt(param));
		
		return result;
	}
	

}

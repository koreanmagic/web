package kr.co.koreanmagic.web.controller.support.webdata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateParamValue {
	
	private Map<String, String> paramValue = new HashMap<>();
	private Map<String, String[]> paramValues = new HashMap<>();
	
	
	public void put(String key, String value) {
		String[] _values = paramValues.get(key); 
		if(_values != null) { // values에 값이 있으면 바로 삽입
			_values = Arrays.copyOf(_values, _values.length + 1);
			_values[_values.length - 1] = value;
			paramValues.put(key, _values);
			return;
		}
		String _value = paramValue.put(key, value);
		if(_value != null) {
			_values = new String[2];
			_values[0] = _value;
			_values[1] = paramValue.remove(key);
			paramValues.put(key, _values);
		}
	}
	
	public Map<String, String> getParamValue() {
		return this.paramValue;
	}
	
	public Map<String, String[]> getParamValues() {
		return this.paramValues;
	}

}

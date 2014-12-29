package kr.co.koreanmagic.hibernate3.legacy.support;

import java.util.Map;

public interface PersisTentCreator<T> {
	

	T creat(Map<String, Object> values);

}

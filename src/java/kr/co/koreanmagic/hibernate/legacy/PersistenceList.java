package kr.co.koreanmagic.hibernate.legacy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import kr.co.koreanmagic.commons.KoReflectionUtils;

import org.hibernate.Session;

/*
 * 영속객체의 모든 리스트를 담아두는 일종의 2차 캐시.
 * 영속객체의 클래스가 key가 되며,
 * DB에 저장된 영속객체의 모든 리스트가 List<T>로 제공된다.
 */
public class PersistenceList {
	
	private static final Map<Class<?>, Data> cache = new HashMap<>();
	
	/*
	 * 세션을 통해 직접 리스트 불러옴
	 */
	public static<T> List<T> cache(Class<T> key, Session session) {
		@SuppressWarnings("unchecked")
		List<T> t = session.createCriteria(key).list();
		
		cache.put(key, new Data(
					t, KoReflectionUtils.getterMethodMap(key)
				));
		
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> List<T> get(Class<T> key) {
		return (List<T>)cache.get(key).getList();
	}
	
	
	/*
	 * 빈의 특정 프로퍼티 값을 검색해준다.
	 * 람다식으로 이 값을 받아 검증한 후 true를 반환해주면, 해당 빈을 반환한다.
	 */
	/*public static<T, V> T search(Class<T> key, String propName, Function<V, Boolean> lambda) {
		Data data = cache.get(key);
		List<T> list = data.getList();
		Method method = data.getMethod(propName);
		if(method == null)
			throw new RuntimeException(key.getSimpleName() + "에서 '" + propName + "' 프로퍼티를 찾을 수 없습니다.");
		
		try {
			for(T t : list) {
				if(lambda.apply((V)method.invoke(t))) {
					return t;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}*/
	
	
	private static class Data {
		private List<?> list = null;
		private Map<String, Method> methods;
		
		private Data(List<?> list, Map<String, Method> methods) {
			this.list = list;
			this.methods = methods;
		}
		
		Map<String, Method> getMethods() {
			return this.methods;
		}
		Method getMethod(String methodName) {
			return methods.get(methodName);
		}
		
		@SuppressWarnings("unchecked")
		<T> List<T> getList() { 
			return (List<T>)this.list;
		}
	}
	
	

}

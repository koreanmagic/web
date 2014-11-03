package kr.co.koreanmagic.web.support.nav;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.web.support.page.PageContextImpl;

/*
 * Navigator 생성기
 * properties등을 통해 메뉴구조를 생성한다.
 * 
 * nav.propertis 샘플 파일을 통해 구조를 파악하라.
 * 
 */
public class NavigatorLoader {
	
	
	public static Navigator loader(Map<?, ?> menuMap, GeneralNavigator rootNav) {
		
		Set<Object> keySet = new TreeSet(menuMap.keySet());
		
		String key = null;
		String[] keys = null,
				paths = null;
		GeneralNavigator parentNav = null;
		
		for(Object _key : keySet) {
			key = _key.toString();
			keys = key.split("\\.");		// .으로 구분되어 있는 키 값을 분할
			paths = paths(menuMap, keys);	// 각 키값에 해당하는 path
			
			if(keys.length == 1) {
				
				rootNav.addMenu(createNavigator(menuMap, key, key, null));
				
			} else {
				int last = keys.length - 1;
				String parentPath = KoStringUtils.join(paths, 0, last, "/");
				parentNav = rootNav.find(parentPath);
				
				parentNav.addMenu(createNavigator(menuMap,
													key,
													keys[last],
													parentNav.pageContext().getPath()
												)
									);
			}
		}
		return rootNav;
	}
	
	private static String[] paths(Map<?, ?> prop, String[] keys) {
		
		String[] result = new String[keys.length];
		String key = null;
		Object pathValue = null;
		
		for(int i=0, l=keys.length; i<l; i++) {
			key = KoStringUtils.join(keys, 0, i+1, ".");
			
			pathValue = prop.get(key);	// 부모 Navigator의 패스를 받아온다.
			
			if(pathValue == null)
				throwException("부모 네비게이터인 [ " + key + " ]이(가) 등록되어 있지 않습니다.");
			
			result[i] = pathValue.toString();
		}
		
		return result;
	}
	
	private static void throwException(String msg) {
		throw new NotFoundNavigatorException(msg);
	}
	
	/*
	 * Navigator 만들기
	 * prop	: path와 name값을 가지고 있는 Map객체
	 * key 	: Map 객체의 키 값. (키 값은 분할하여 PageContext의 Name값이 되기도 한다.) 
	 */
	private static GeneralNavigator createNavigator(Map<?, ?> prop, String key, String name, String parentPath) {
		String path = prop.get(key).toString();
		parentPath = parentPath != null ? parentPath + "/" : "";	// 앞에 있는 path와 합친다.
		
		if(path == null)
			throw new NullPointerException("properties에 [" + key + "] 키에 해당하는 값이 없습니다.");
		PageContextImpl context = new PageContextImpl(parentPath + path)
										.setName(name);
		return new GeneralNavigator(context);
	}
	
	private static<T> T log(T obj) {
		System.out.println(">> " + obj);
		return obj;
	}

}

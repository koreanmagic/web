package kr.co.koreanmagic.web2.servlet.tag;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import kr.co.koreanmagic.commons.KoStringUtils;

/*
 * forEach 루틴를 구현한 태그
 * 
 * value값으로 넣어준 request key값을 통해 객체를 가지고 온다.
 * 객체는 아래와 같은 값이 될 수 있다.
 * 1) 배열			(내부 인자만큼 반복)
 * 2) Collection	(내부 인자만큼 반복)
 * 3) 단일 객체		(1번 반복)
 * 4) null			(반복되지 않는다. 태그 안의 내용은 html로 출력되지 않는다.)
 *
 * ★ 반복시 this.name + "_index" 값은 현재 루프의 순번이다. 0부터 시작  (프리마커 API 흉내)
 *
 * 배열이나 Collection은 length만큼 반복되며 각 값은 this.name값으로 Request.Attribute에 담긴다.
 * 단일객체는 1번만 반복되며,
 * null의 경우는 그냥 통과한다. (태그의 안쪽 바디는 html로 출력되지 않는다.)
 * 
 * ★★★
 * 이때 Reqeust에 this.name을 키로 하는 값이 이미 저장되어 있었다면,
 * 이 값은 루프 후에 다시 복원된다. 
 */
public class ForEach extends SimpleTagSupport {
	
	private Object value;
	private Object indexVal;
	private String name = "item";
	private String indexName;
	
	// request Attribute 키 값
	public void setValue(Object value) {
		this.value = value;
	}
	// 바디컨텐츠 안에서 사용한 request Attribute 키 값
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		
		// request에서 값을 꺼내온다.
		HttpServletRequest req = (HttpServletRequest)((PageContext)getJspContext()).getRequest();
		
		Object obj = null;
		String key = value.toString();
		int pos = key.indexOf(".");
		
		
		if(pos != -1) {

			// 1) null값의 경우 리턴
			obj = req.getAttribute(key.substring(0, pos));
			if(obj == null) return;
			
			// value1.value2 등으로 표시된 키값이 들어오면 리플렉션을 통해 객체 그래프를 탐색한다.
			obj = get(obj, key.substring(pos + 1)); 
			if(obj == null) return;
			
		} else {
			obj = req.getAttribute(key);
			// 2) null값의 경우 리턴
			if(obj == null) return;
		}
		
		// 배열인지 List인지 확인을 위한 작업
		Class<?> type = obj.getClass();
		
		this.value = req.getAttribute(this.name);	// 다시 돌려놓을 원본값을 저장해둔다.
		this.indexName = this.name + "_index";
		this.indexVal = req.getAttribute(this.indexName);
		
		/*
		 * 배열인지, 콜렉션인지 확인해본다.
		 */
		if(type.isArray()) array((Object[])obj, req);
		else if(obj instanceof Collection) collection((Collection<?>)obj, req);
		else if(obj != null) oneValue(obj, req);

	}
	
	private void oneValue(Object obj, HttpServletRequest req) throws JspException, IOException {
		req.setAttribute(this.name, obj);
		req.setAttribute(indexName, 0);
		getJspBody().invoke(null);
		
		restore(req);	// 값을 다시 돌려놓는다.
	}
	
	private void array(Object[] array, HttpServletRequest req) throws JspException, IOException {
		int i = 0;
		for(Object item : array) {
			req.setAttribute(this.name, item);
			req.setAttribute(indexName, i++);
			getJspBody().invoke(null);
		}
		restore(req);	// 값을 다시 돌려놓는다.
	}
	
	private void collection(Collection<?> col, HttpServletRequest req) throws JspException, IOException {
		int i = 0;
		for(Object item : col) {
			req.setAttribute(this.name, item);
			req.setAttribute(indexName, i++);
			getJspBody().invoke(null);
		}
		restore(req);	// 값을 다시 돌려놓는다.
	}
	
	
	/*
	 * 맵인지 일반 POJO객체인지 확인한다.
	 */
	private Object get(Object obj, String name) {
		return (obj instanceof Map) ? map(obj, name) : reflection(obj, name);
	}
	
	// 맵일 경우는 바로 리턴
	@SuppressWarnings("unchecked")
	private Object map(Object obj, String name) {
		return ( (Map<Object, Object>)obj ).get(name);
	}
	
	
	// 리플렉션
	private Object reflection(Object obj, String name) {
		String[] names = name.split("\\.");
		Method method = null;
		
		try {
			for(int i=0, l=names.length; i<l; i++) {
				name = KoStringUtils.propertyName(names[i], "get");
				method = obj.getClass().getMethod(name);
				
				// null값이 나올 경우 리턴한다. 루프를 중지하고 반환한다.
				obj = method.invoke(obj);
				if(obj == null) return null;
			}
		} catch(NoSuchMethodException e) {
			// 메서드가 없을 경우 리턴한다. name1.name2 에서 name2의 프로퍼티 값이 null이거나 프로퍼티 자체가 없을 경우다.
			return null;
		} catch(Exception e) {
			throw new RuntimeException(obj + "의 객체그래프(" + value + ") 탐색 중 실패" );
		}
		return obj;
	}
	
	// 어트리뷰트 값 복원
	private void restore(HttpServletRequest req) {
		if(this.value != null)
			req.setAttribute(this.name, this.value);
		if(this.indexVal != null)
			req.setAttribute(indexName, indexVal);
	}

}

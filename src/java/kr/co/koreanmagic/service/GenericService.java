package kr.co.koreanmagic.service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.Table;

import kr.co.koreanmagic.commons.KoReflectionUtils;
import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.dao.GeneralDao;
import kr.co.koreanmagic.web.service.Service;
import kr.co.koreanmagic.web.support.board.PagingQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/*
 * 2014-10-22 am10:05
 * 
 * class-proxy를 사용할 경우, 인터페이스가 아니라 구현객체를 직접 호출해야 @Transactional이 동작한다.
 * 이유는 모른다..
 */
@Transactional
public abstract class GenericService<T, P extends Serializable> implements Service<T, P> {
	
	private GeneralDao<T, P> dao;
	private Map<String, PropertyDescriptor> descriptors;
	private String tableName;
	
	protected void setDao(GeneralDao<T, P> dao) {
		this.dao = dao;
	}
	public GeneralDao<T, P> getDao() {
		return this.dao;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	@Autowired
	private void init(SessionFactory factory) throws Exception {
		Class<T> clazz = KoReflectionUtils.getActualType(getClass(), 0);
		setDao(new GeneralDao<T, P>(clazz, factory));
		
		descriptors = new HashMap<>(); 
		for(PropertyDescriptor descriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
			descriptors.put( descriptor.getName(), descriptor );
		}
		
		this.tableName = clazz.getAnnotation(Table.class).name();
	}
	
	public Map<String, PropertyDescriptor> getDescriptors() {
		return this.descriptors;
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 리플렉션으로 Data Map 가지고 오기 ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@Transactional(readOnly=true)
	public Object[] getValueMapById(P id, String[] properties, Function<Object, String> lamda) {
		return getValueMap(get(id), properties, lamda);
	}
	
	// 단일 객체
	@Transactional(readOnly=true)
	public Object[] getValueMap(Object bean, String[] properties, Function<Object, String> lamda) {
		
		Object[] result = new Object[2];
		result[0] = bean;
		
		if(bean == null) throw new NullPointerException();
		
		lamda = (lamda == null) ?
				(v) -> { return v.toString(); }
				: lamda;
		
		Map<String, String> map = new HashMap<>();
		
		Object value = null;
		String stringValue = null;
		
		for(String property : properties) {

			value = value(property, bean);
			
			if(value == null) continue;
			if( (stringValue = lamda.apply(value)) == null ) continue;
			
			map.put(property, stringValue);
		}
		
		result[1] = map;
		
		return result;
	}

	// name.id로 되는 객체 그래프 탐색
	private Object value(String prop, Object bean) {
		String[] props = prop.split("\\.");
		PropertyDescriptor des = descriptors.get(props[0]);
		
		if(des == null)
			throw new IllegalArgumentException(bean.getClass().getSimpleName() + "의 " + props[0] + "는 없는 프로퍼티입니다.");
		
		Object obj = null; 
		String name = null;
		
		try {
			Method method = null;
			obj = des.getReadMethod().invoke(bean);
			for(int i=1, l=props.length; i<l; i++) {
				if(obj == null) break;
				name = props[i];
				method = obj.getClass().getMethod(  KoStringUtils.propertyName(name, "get")  );
				// null값이 나올 경우 리턴한다. 루프를 중지하고 반환한다.
				obj = method.invoke(obj);
			}
		} catch(NoSuchMethodException e) {
			throw new RuntimeException(obj.getClass().getSimpleName() + "의 " + name+ "는 없는 메서드입니다.", e);
		} catch(Exception e) {
			throw new RuntimeException(prop + " ==> " + obj + "의 객체그래프 탐색 중 실패", e);
		}
		return obj;
	}
	
	// 리스트 객체
	@Transactional(readOnly=true)
	public Object[] getValueListMap(List<T> list, String[] properties, Function<Object, String> lamda) {
		Object[] result = new Object[2];
		List<Object> beans = new ArrayList<>();
		List<Map<String, String>> maps = new ArrayList<>();
		
		Object[] valueMap = null;
		
		for( T bean : list ) {
			valueMap = getValueMap(bean, properties, lamda);
			beans.add( valueMap[0] );
			maps.add( (Map<String, String>)valueMap[1] );
		}
		
		result[0] = beans;
		result[1] = maps;
		return result;
	}
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▲ 리플렉션으로 Data Map 가지고 오기 ▲  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	
	
	public<V> V query(boolean isArray, String query) {
		return getDao().query(isArray, query);
	}
	
	@Override
	public T add(T entity) {
		return (T)getDao().makePersistent(entity);
	}
	
	@Override
	public T update(T entity) {
		return getDao().makePersistent(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().makeTransient(entity);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> getAll() {
		return (List<T>)getDao().findAll();
	}
	
	@Override
	public T get(P primaryKey) {
		return (T)getDao().findById(primaryKey);
	}
	public T get(P primaryKey, boolean readOnly) {
		return (T)getDao().findById(primaryKey, readOnly);
	}
	
	@SuppressWarnings("unchecked")
	public T load(P primarykey) {
		return (T)getDao().getSession().load(getServiceClass(), primarykey);
	}
	
	@Transactional(readOnly=true)
	public List<T> getList(PagingQuery paging) {
		return getList(paging.getStart(), paging.getLimit(), paging.getOrder());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<T> getList(int start, int limit, String orderBy) {
		return getDao().getList(start, limit, orderBy);
	}
	
	// 스칼라 값 가지고 오기 by Example
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Map<String, Object> scalaMap(T bean, String...properties) {
		return (Map<String, Object>)getDao().criteria()
					.add( Example.create(bean) )
					.setProjection( projectionList(properties) )
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
	}
	
	// 스칼라 값 가지고 오기 by primaryKey
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Map<String, Object> scalaMapById(P id, String...properties) {
		return (Map<String, Object>)getDao().criteria()
				.add(Restrictions.idEq(id))
				.setProjection( projectionList(properties) )
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.uniqueResult();
	}
	
	private ProjectionList projectionList(String[] properties) {
		ProjectionList list = Projections.projectionList();
		
		String alias = null;
		int index = -1;
		for(String property : properties ) {
			
			// map의 key값은 공백을 사이에두고 쓰면 된다. sql의 as 사용과 비슷하다고 생각하면 된다.
			if( (index = property.indexOf(" ")) != -1) {
				alias = property.substring(index + 1);
				property = property.substring(0, index);
			}
			else alias = property;
			
			list.add(Property.forName(property).as(alias));
		}
		return list;
	}
	
	@Override
	@Transactional(readOnly=true)
	public long rowCount() {
		return getDao().rowCountNative();
	}
	@Override
	public Class<T> getServiceClass() {
		return getDao().getPersistentClass();
	}

	@Override
	public void flush() {
		getDao().flush();
	}
	
	@SuppressWarnings("unchecked")
	protected<V> V restriction(boolean isArray, Criterion...criterions) {
		return (V)getDao().findByCriteria(isArray, criterions);
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  GenericService 자체구현 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	protected Session currentSession() {
		return getDao().getFactory().getCurrentSession();
	}
	
	protected<V> V nullValue(V obj) {
		return obj != null && obj.equals("null") ? null : obj;
	}
	
}

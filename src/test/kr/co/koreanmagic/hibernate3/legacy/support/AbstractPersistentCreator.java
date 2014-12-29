package kr.co.koreanmagic.hibernate3.legacy.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import kr.co.koreanmagic.commons.KoReflectionUtils;
import kr.co.koreanmagic.dao.GenericDao;
import kr.co.koreanmagic.service.GenericService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;

public abstract class AbstractPersistentCreator<T> implements PersisTentCreator<T> {

	@Autowired private List<? extends GenericDao<?, ? extends Serializable>> daoList;
	@Autowired private List<? extends GenericService<?, ? extends Serializable>> serviceList;
	@Autowired private SessionFactory factory;
	
	@Autowired ConversionService conversionService;
	private GenericService<T, Serializable> service;
	
	private NameConvertor nameConvertor;
	private Class<T> clazz;
	
	
	@PostConstruct
	public void init() {
		this.clazz = KoReflectionUtils.getActualType(getClass(), 0);
	}
	
	
	private Map<String, Object> map;
	private String value;
	protected void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	/* 값 체크 || null값이 아니면 value에 담는다. */
	protected boolean check(String key) {
		this.value = null;

		Object obj = map.get(key);
		if(obj == null)
			return false;
		
		String result = obj.toString();
		
		if(result.length() < 1)
			return false;
	
		this.value = result;
		return true;
	}
	
	protected String getValue() {
		return this.value;
	}
	
	protected NameConvertor getNameConvertor() {
		if(this.nameConvertor == null) nameConvertor =  NameConvertManager.get(this.clazz);
		return nameConvertor;
	}
	protected NameConvertor getNameConvertor(Class<?> clazz) {
		return NameConvertManager.get(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public<V extends GenericService<T, ? extends Serializable>> V getService() {
		if(this.service == null) this.service = findService(this.clazz);
		return (V)this.service;
	}
	
	/* 객체 저장 */
	protected T add(T entity) {
		return getService().add(entity);
	}
	
	public List<? extends GenericService<?, ? extends Serializable>> serviceList() {
		return this.serviceList;
	}
	
	/*
	 * 스프링 빈에 등록된 모든 Dao중에 해당하는 영속화객체를 담당하는 Dao 골라내기
	 */
	@SuppressWarnings("unchecked")
	public<V extends GenericService<?, ? extends Serializable>, T, P extends Serializable> V findService(T entity) {
		for(GenericService<?, ? extends Serializable> service : serviceList) {
			System.out.println(service.getServiceClass());
			if(service
					.getServiceClass()
					.equals(entity))
				return (V)service;
		}
		throw new RuntimeException(entity + "에 해당하는 service가 없습니다.");
	}
	
	/*
	 * 스프링 빈에 등록된 모든 Service중에 해당하는 영속화객체를 담당하는 Service 골라내기
	 */
	@SuppressWarnings("unchecked")
	protected<V extends GenericDao<?, ? extends Serializable>, T, P extends Serializable> V findDao(T entity) {
		for(GenericDao<?, ? extends Serializable> dao : daoList) {
			if(dao.getPersistentClass().equals(entity))
				return (V)dao;
		}
		throw new RuntimeException(entity + "에 해당하는 dao가 없습니다.");
	}
	
	
	/*
	 * 배열이 비어있는지, 빈 문자인지 확인
	 * 있으면 문자열 반환,
	 * 없으면 null 반환
	 */
	protected String nullCheck(String[] values, int i) {
		String value = null;
		if(values.length > i && (value = values[i]) != null && value.length() != 0) {
			return value;
		}
		return null;
	}
	
	protected Session session() {
		return factory.getCurrentSession();
	}
	@SuppressWarnings("unchecked")
	protected<P> P get(Class<P> clazz, Serializable primary) {
		return (P)session().get(clazz, primary);
	}
	@SuppressWarnings("unchecked")
	protected<P> P load(Class<P> clazz, Serializable primary) {
		return (P)session().load(clazz, primary);
	}
	
	protected<V> V convert(Object value, Class<V> v) {
		return conversionService.convert(value, v);
	}
	
	
}

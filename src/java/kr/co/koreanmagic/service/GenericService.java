package kr.co.koreanmagic.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import kr.co.koreanmagic.commons.DateUtils;
import kr.co.koreanmagic.commons.ReflectionUtils;
import kr.co.koreanmagic.dao.GeneralDao;
import kr.co.koreanmagic.web.service.Service;
import kr.co.koreanmagic.web.support.board.PagingRequest;

import org.hibernate.Criteria;
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
		Class<T> clazz = ReflectionUtils.getActualType(getClass(), 0);
		setDao(new GeneralDao<T, P>(clazz, factory));
		
		this.tableName = clazz.getAnnotation(Table.class).name();
	}
	
	
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
	public T refresh(T entity) {
		getDao().refresh(entity);
		return entity;
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
	
	
	public List<T> getList(PagingRequest req) {
		return getList(getDao().criteria(), req);
	}
	
	public List<T> getList(Criteria criteria, PagingRequest req) {
		return getDao().getList(criteria,		
										req.getStart(),
										req.getLimit(),
										req.getOrder()
									);
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
	
	// 테이블 명 알아내기
	public static final String getTableName(Object obj) {
		return obj.getClass().getAnnotation(Table.class).name();
	}
}

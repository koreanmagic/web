package kr.co.koreanmagic.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Table;

import kr.co.koreanmagic.commons.KoReflectionUtils;
import kr.co.koreanmagic.hibernate3.dao.AbstractHibernate3Dao;
import kr.co.koreanmagic.hibernate3.work.NativeWork;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/*
 * koreanmagic-hibernate2의 제네릭Doa의 메서드에 @Transactional을 추가한 버전
 * 
 */
public abstract class GenericDao<T, P extends Serializable> extends AbstractHibernate3Dao<T, P> {

	
	@Autowired private SessionFactory factory;
	private Class<T> persistentClass;
	

	@PostConstruct
	public void init() {
		this.persistentClass = KoReflectionUtils.getActualType(getClass(), 0);
	}
			
	
	protected SessionFactory getFactory() {
		return this.factory;
	}

	@Override
	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}
	
	@Override
	@Transactional
	public T findById(P id) {
		return super.findById(id);
	}

	@Override
	@Transactional
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		return super.findByExample(exampleInstance, excludeProperty);
	}

	@Override
	@Transactional
	public T makePersistent(T entity) {
		return super.makePersistent(entity);
	}

	@Override
	@Transactional
	public void makeTransient(T entity) {
		super.makeTransient(entity);
	}

	@Override
	@Transactional
	public long rowCount() {
		return super.rowCount();
	}
	
	@Override
	@Transactional
	public <V> V doWork(NativeWork<V> work) {
		return super.doWork(work);
	}
	
	
	@Override
	@Transactional
	public<V> V findByCriteria(boolean isArray, Criterion... criterion) {
		return super.findByCriteria(isArray, criterion);
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  GenericDao 자체구현 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	

	/*
	 * 상속구조의 객체인 경우, 어떤 SELECT 쿼리를 날리던 조인이 걸린다.
	 * 따라서 이 심플한 SQL문을 통해 성능에 효율을 기한다.
	 */
	@Transactional
	public long rowCountNative() {
		BigInteger obj = (BigInteger)currentSession()
							.createSQLQuery("SELECT COUNT(*) FROM " + getEntityName())
							.uniqueResult();
		return obj.longValue();
	}
	
	/* WHERE prop = value */
	public<V> V eq(boolean isArray, String propertyName, Object value) {
		return findByCriteria(isArray, Restrictions.eq(propertyName, value));
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  GenericDao Utils  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * 영속 클래스를 통해 DB Table name을 알아온다.
	 */
	private String entityName;
	
	private String getEntityName() {
		if(entityName == null) {
			Table entity = getPersistentClass().getAnnotation(Table.class);
			if(entity != null)
				entityName = entity.name();
			else
				entityName = StringUtils.uncapitalize(getPersistentClass().getSimpleName());
		}
		return entityName;
	}




}

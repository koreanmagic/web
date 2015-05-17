package kr.co.koreanmagic.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Table;

import kr.co.koreanmagic.hibernate3.dao.AbstractHibernate3Dao;
import kr.co.koreanmagic.hibernate3.work.NativeWork;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

/*
 * koreanmagic-hibernate2의 제네릭Doa의 메서드에 @Transactional을 추가한 버전
 * 
 */
public class GeneralDao<T, P extends Serializable> extends AbstractHibernate3Dao<T, P> {

	
	private SessionFactory factory;
	
	public GeneralDao( Class<T> persistentClass, SessionFactory factory ) {
		super(persistentClass);
		this.factory = factory;
	}
	
	@Override
	public SessionFactory getFactory() {
		return this.factory;
	}
	
	public Session getSession() {
		return currentSession();
	}

	public Criteria criteria() {
		return super.criteria();
	}
	
	
	@Override
	public T findById(P id) {
		return super.findById(id);
	}
	
	@Override
	public List<T> findAll() {
		return super.findAll();
	}

	@Override
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		return super.findByExample(exampleInstance, excludeProperty);
	}
	
	@Override
	public List<T> getList(int start, int limit, String orderBy) {
		return super.getList(start, limit, orderBy);
	}
	

	@Override
	public T makePersistent(T entity) {
		return super.makePersistent(entity);
	}
	
	@Override
	public void makeTransient(T entity) {
		super.makeTransient(entity);
	}

	@Override
	public long rowCount() {
		return super.rowCount();
	}
	
	@Override
	public <V> V doWork(NativeWork<V> work) {
		return super.doWork(work);
	}
	
	
	@Override
	public<V> V findByCriteria(boolean isArray, Criterion... criterion) {
		return super.findByCriteria(isArray, criterion);
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  GeneralDao 자체구현 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	

	/*
	 * 상속구조의 객체인 경우, 어떤 SELECT 쿼리를 날리던 조인이 걸린다.
	 * 따라서 이 심플한 SQL문을 통해 성능에 효율을 기한다.
	 */
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

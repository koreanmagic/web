package kr.co.koreanmagic.service;

import java.io.Serializable;
import java.util.List;

import kr.co.koreanmagic.commons.KoReflectionUtils;
import kr.co.koreanmagic.dao.GenericDao;
import kr.co.koreanmagic.web.dao.Dao;
import kr.co.koreanmagic.web.service.Service;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * 2014-10-22 am10:05
 * 
 * class-proxy를 사용할 경우, 인터페이스가 아니라 구현객체를 직접 호출해야 @Transactional이 동작한다.
 * 이유는 모른다..
 */
public abstract class GenericService<T, P extends Serializable> implements Service<T, P> {
	
	private GenericDao<T, P> dao;
	
	protected void setDao(GenericDao<T, P> dao) {
		this.dao = dao;
	}
	public GenericDao<T, P> getDao() {
		return this.dao;
	}
	
	@Autowired
	@SuppressWarnings("unchecked")
	private void init(List<Dao<?,?>> daos) {
		Class<T> clazz = KoReflectionUtils.getActualType(getClass(), 0);
		for(Dao<?,?> dao : daos) {
			if(dao.getPersistentClass().equals(clazz)) {
				setDao((GenericDao<T, P>)dao);
			}
				
		}
	}
	
	
	@Override
	public T add(T entity) {
		return getDao().makePersistent(entity);
	}

	@Override
	public void update(T entity) {
		getDao().makePersistent(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().makeTransient(entity);
	}

	@Override
	public List<T> getAll() {
		return getDao().findAll();
	}

	@Override
	public T get(P primaryKey) {
		return getDao().findById(primaryKey);
	}
	
	@Override
	public List<T> getList(int start, int limit) {
		return getDao().getList(start, limit);
	}
	
	@Override
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
}

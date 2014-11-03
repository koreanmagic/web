package kr.co.koreanmagic.web.dao;

import java.util.List;

public interface Dao<T, P> {
	
	T findById(P id);
	List<T> findAll();
	List<T> getList(int start, int limit);
	List<T> findByExample(T exampleInstance, String...excludeProperty);
	T makePersistent(T entity);
	void makeTransient(T entity);
	Class<T> getPersistentClass();
	long rowCount();
	
	void flush();
	void clear();
	
	

}

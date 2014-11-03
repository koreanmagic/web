package kr.co.koreanmagic.web.service;

import java.util.List;

public interface Service<T, P> {

	T add(T entity);
	void update(T entity);
	void delete(T entity);
	List<T> getAll();
	List<T> getList(int start, int limit);
	T get(P primaryKey);
	
	long rowCount();
	
	Class<T> getServiceClass();
	
	/* 모든 serivce 객체는 기본 Entity객체를 만들거나 기본객체를 반환할 의무가 있다. */
	T getInitalBean();
	T getDefaultBean();
	
	void flush();
}

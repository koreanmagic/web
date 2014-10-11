package kr.co.koreanmagic.web.service;



// T : domain 타입  // P : Primary Key 타입
public interface GeneralService<T, P> {
	
	
	/* SELECT */
	T get(P primaryKey);
	
	/* UPDATE */
	int update(T domain);
	
	/* INSERT */
	int insert(T domain);
	
	/* DELETE  */
	int delete(T domain);

}

package kr.co.koreanmagic.web.db.mybatis.mapper.extension;



public interface GenericMapper<T, P> {
	
	int deleteByPrimaryKey(T record);
	int insert(T record);
	int insertSelective(T record);
	T selectByPrimaryKey(P id);
	int updateByPrimaryKeySelective(T record);
	int updateByPrimaryKey(T record);

}

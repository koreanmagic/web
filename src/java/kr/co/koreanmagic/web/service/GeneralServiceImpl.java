package kr.co.koreanmagic.web.service;

import kr.co.koreanmagic.web.db.mybatis.mapper.extension.GenericMapper;

public class GeneralServiceImpl<T, P> implements GeneralService<T, P> {
	
	private GenericMapper<T, P> mapper;
	
	protected GeneralServiceImpl(GenericMapper<T, P> mapper) {
		this.mapper = mapper;
	}
 
	protected GenericMapper<T, P> getMapper() {
		return this.mapper;
	}
	
	@Override
	public T get(P primaryKey) {
		return getMapper().selectByPrimaryKey(primaryKey);
	}

	@Override
	public int update(T domain) {
		return getMapper().updateByPrimaryKeySelective(domain);
	}

	@Override
	public int insert(T domain) {
		return getMapper().insertSelective(domain);
	}

	@Override
	public int delete(T domain) {
		return getMapper().deleteByPrimaryKey(domain);
	}

}

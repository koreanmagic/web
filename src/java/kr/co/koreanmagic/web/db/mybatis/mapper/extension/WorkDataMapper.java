package kr.co.koreanmagic.web.db.mybatis.mapper.extension;

import java.util.List;

import kr.co.koreanmagic.web.domain.WorkData;

public interface WorkDataMapper<T extends WorkData> extends GenericMapper<T, Integer> {
	
	List<T> selectByWorkId(String workId);
	int countByWorkId(String workId);
	int addCount(Integer id);
	int count(String workId);
	int insertSelective(WorkData record);

}

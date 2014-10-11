package kr.co.koreanmagic.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.koreanmagic.web.db.mybatis.mapper.WorkConditionMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkListTypeMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkCondition;
import kr.co.koreanmagic.web.domain.WorkListType;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class WorkService extends GeneralServiceImpl<Work, String> {

	

	Logger logger = Logger.getLogger(getClass());
	
	private final WorkMapper mapper;
	@Autowired WorkConditionMapper workConditionmapper;
	@Autowired WorkListTypeMapper workListTypeMapper;
	
	@Autowired
	protected WorkService(WorkMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ SELECT ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▽ 검색기능 ▽ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 거래처 이름으로 검색
	public List<Work> searchByCustomer(WorkCondition condition, String customer, int start, int size) {
		return mapper.searchByCustomer(condition.getId(), "%"+customer+"%", start, size);
	}
	// 거래처 이름으로 검색 갯수
	public Integer searchByCustomerLen(WorkCondition condition, String customer) {
		return mapper.searchByCustomerLen(condition.getId(), "%"+customer+"%");
	}
	
	
	
	// 컨디션별 전체 리스트
	public Integer getCountListByCondition(WorkCondition condition) {
		return mapper.countListByCondition(condition.getId());
	}
	
	// 전체 리스트를 대상으로 페이징
	public List<Work> getBetweenList(int start, int size) {
		return mapper.selectLimitAll(start, size);
	}

	
	// id, 즉 시퀀스 가지고 오기 
	public void setUniqueId(Work work) {
		String id = mapper.sequence(work.getInsertTime());
		work.setId(id);
	}
	// 통계 가지고 오기
	public Map<String, Integer> workListStats() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		mapper.workListStats(map);
		return map;
	}
	
	
	// WHERE 이하 직접 작성해서 리스트 가지고 오기
	public List<Work> getListByCustomSQL(String where, int start, int size) {
		return mapper.selectListByCostomSQL(where, start, size);
	}
	public Integer getCountByCustomSQL(String where) {
		return mapper.selectCountByCostomSQL(where);
	}	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒ ▽ Work_condition ▽ ▒▒▒▒▒▒▒▒▒▒ */
	// 진행상황 리스트
	public List<WorkCondition> workConditionList() {
		return workConditionmapper.getAll();
	}
	// 진행상황 1개 가지고 오기
	public WorkCondition getWorkCondition(Integer id) {
		return workConditionmapper.selectByPrimaryKey(id);
	}
	
	// 컨디션 값에 따른 게시물 (긴급 게시물을 제외한다.)
	public List<Work> getListByCondition(WorkCondition condition, int start, int size) {
		return mapper.selectListByCondition(condition.getId(), start, size);
	}
	// 컨디션 값에 따른 긴급 게시물
	public List<Work> getEmergencytWorks(WorkCondition condition) {
		return mapper.selectEmergencytWorks(condition.getId());
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒ ▽ Work_list_type ▽ ▒▒▒▒▒▒▒▒▒▒ */
	// 진행상황 리스트
	public List<WorkListType> workListTypes() {
		return workListTypeMapper.selectAll();
	}
	// 진행상황 1개 가지고 오기
	public WorkListType getWorkListType(Integer id) {
		return workListTypeMapper.selectByPrimaryKey(id);
	}
	

	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ UPDATE ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	// 조회수 올리기
	public int read(Work work) {
		int i = mapper.read(work.getId());
		work.setReadCount(work.getReadCount() + i);
		return i;
	}
	// 업데이트 타임 갱신
	public int refesh(Work work) {
		return mapper.refresh(work.getId());
	}
	
	// 읽음표시
	public void readCheckOn(Work work) {
		if(!work.getReadCheck()) // 리드체크가 false 일때 --> true로 바꿈
			readCheck(work);
	}
	// 안읽음표시
	public void readCheckOff(Work work) {
		if(work.getReadCheck()) // 리드체크가 true 일때 --> false로 바꿈
			readCheck(work);
			
	}
	private void readCheck(Work work) {
		if(mapper.updateReadCheck(work.getId()) == 1)
			work.setReadCheck(!work.getReadCheck());
		else
			throw new RuntimeException("리드체크 에러");
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒ ▽ Work_list_type ▽ ▒▒▒▒▒▒▒▒▒▒ */
	// 워크리스트 타입 바꾸기
	public int setListType(WorkListType workListType, Work work) {
		return mapper.setListType(workListType.getId(), work.getId());
	}
	
	// 워크리스트타입 노멀로 바꾸기
	public int updateNormal(Work work) {
		return mapper.setListType(1, work.getId());
	}
	
	public int countListByCondition(WorkCondition condition) {
		return mapper.countListByCondition(condition.getId());
	}
	
	
	
}

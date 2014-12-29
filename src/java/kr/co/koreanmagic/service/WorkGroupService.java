package kr.co.koreanmagic.service;

import java.sql.Date;
import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkGroup;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.service.RestrictionCommon;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkGroupService extends GenericService<WorkGroup, String> {
	
	@Autowired WorkStateService workStateService;
	
	
	
	public WorkGroup createBean(WorkState state) {
		WorkGroup workGroup = new WorkGroup();
		workGroup.setState(state);
		return workGroup;
	}
	@Override
	public WorkGroup getInitalBean() { 
		return createBean(workStateService.getInitalBean()); 
	}
	@Override
	public WorkGroup getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	

	/* 거래처에 해당하는 모든 작업리스트 불러오기 */
	public List<WorkGroup> listByCustomer(Customer customer) {
		return restriction(true, Restrictions.eq("customer", customer));
	}
	public List<WorkGroup> listByCustomer(WorkGroup workGroup) {
		return listByCustomer(workGroup.getCustomer());
	}
	
	/* 기간 조회 */
	public List<WorkGroup> listByDate(String start, String end) {
		return restriction(true, RestrictionCommon.betweenDate("insertTime", start, end));
	}
	public List<WorkGroup> listByDate(Date start, Date end) {
		return restriction(true, RestrictionCommon.betweenDate("insertTime", start, end));
	}
	
	/* 작업 상태별 조회 */
	public List<WorkGroup> listByWorkState(WorkState workState) {
		return restriction(true, Restrictions.eq("state", workState));
	}
	
	/* 제목 조회 */
	public List<WorkGroup> listBySubject(String subject) {
		return restriction(true, Restrictions.like("subject", subject, MatchMode.ANYWHERE));
	}
	
	
	/* 읽음표시 */
	public void readCheck(WorkGroup workGroup) {
		//workGroup.setReadCheck(true);
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Work 관련 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	public Work createDefaultWork(Subcontractor subcontractor) {
		Work work = new Work();
		work.setSubconstractor(subcontractor);
		return work;
	}
	
			

}

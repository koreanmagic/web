package kr.co.koreanmagic.service;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kr.co.koreanmagic.commons.DateUtils;
import kr.co.koreanmagic.commons.StringUtils;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkBackUp;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.service.boardlist.WorkBoard;
import kr.co.koreanmagic.service.example.WorkExample;
import kr.co.koreanmagic.service.transformer.WorkStateCount;
import kr.co.koreanmagic.web.support.board.PagingRequest;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@SuppressWarnings("unchecked")
public class WorkService extends GenericService<Work, String> {

	@Autowired WorkResourceFileService resourceService;

	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ - ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ - ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	
	// ID를 담아서 내보낸다.
	@Override
	public Work getInitalBean() {
		Work work =  new Work();
		return work;
	}
	
	@Transactional
	public void merge(Work work) {
		getDao().getSession().merge(work);
	}
	

	@Override
	public Work getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	@Transactional
	public Work getInitialValues(String primaryKey) {
		Work work = get(primaryKey, true);
		Hibernate.initialize(work.getResourceFile());
		return work;
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ boardList ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	// 작업상황 리스트
	// eq-같다  bt-사이값  nb-사이값제외  ge-이상  le-이하  gt-초과  lt-미만  ne-같지않다 
	
	// 검색기능
	@Transactional(readOnly=true)
	public WorkBoard getList0(WorkExample example, WorkBoard workBoard) {
		
		
		Map<String, Criterion> criterions = example.getExamples( currentSession() );	// Example을 담은 리스트
		
		Criterion workExample = example.getOwnerExample();
		Criterion dateCriterion = example.getDateCriterion("insertTime");	// startDate, endDate 쿼리가 있을 경우
		
		WorkState state = example.getState();
		
		Integer[] stateCount = null;	// 해당 스테이트 전체 갯수 구할때 사용
		
		Criteria criteria = null;
		
		// 1) 오늘 변경된 state 신규 갯수
		criteria = criteriaForCount(workExample)
							.add(Restrictions.ge("stateTime", DateUtils.localToDate(LocalDateTime.now().minusDays(1))));
		workBoard.setTodayCount(  getStateCount( 
											addExamples(criteria, criterions).add(dateCriterion) ).get()
										);
		
		
		// 2) 조건에 해당하는 전체 state 갯수
		criteria = criteriaForCount(workExample);
		stateCount = getStateCount( addExamples(criteria, criterions).add(dateCriterion) ).get();
		workBoard.setStateCount(  stateCount  );
		
		
		// 3) 리스트 구하기
		criteria = addExamples( 
							criteriaForList(example.getOwnerExample())
												.add( example.getStateCriterion() ),		// 리스트를 가지고 올 해당 state를 설정한다.
							criterions
						).add(dateCriterion);
		
		workBoard.setList( getList(criteria, workBoard) );
		workBoard.setRowCount( stateCount[state.getId() - 1] );
		
		return workBoard;
		
	}
	
	
	// state별 카운트 구하기
	private WorkStateCount getStateCount( Criteria criteria ) {
		return (WorkStateCount)criteria.setProjection(Projections.projectionList()
										.add(Projections.count("this.id").as("count"))
										.add(Projections.groupProperty("this.workState.id").as("id"))
									)
									.setResultTransformer(new WorkStateCount())
									.uniqueResult();
	}
	
	
	private Criteria addExamples(Criteria criteria, Map<String, Criterion> list) {
		
		Criterion criterion = null;
		String associationPath = null;
		
		for(Entry<String, Criterion> entry : list.entrySet()) {
			
			associationPath = entry.getKey();
			criterion = entry.getValue();
			
			// Example일 경우에는 예제로 등록, 아닐 경우에는 엔터티 검색
			if( criterion instanceof Example )
				criteria.createCriteria(associationPath ).add(entry.getValue() );
			else
				criteria.add( entry.getValue() );
		}
		
		return criteria;
	}
	
	// 카운트계산용 크리테리아
	public Criteria criteriaForCount(Criterion workExample) {
		Criteria criteria = getDao().criteria();
		return workExample == null ? criteria : criteria.add(workExample);
	}
	
	
	// 리스트용 크리테리아
	public Criteria criteriaForList() {
		return criteriaForList(null);
	}
	public Criteria criteriaForList(Criterion workExample) {
		Criteria criteria = getDao().criteria()
				
				.setFetchMode("customer", FetchMode.JOIN)
				.setFetchMode("subcontractor", FetchMode.JOIN)
				.setFetchMode("manager", FetchMode.JOIN)
				
				// manager는 null이 있을 수 있기때문에 inner join으로 가지고 오면 리스트가 빠질 수 있다.
				;
		return workExample == null ? criteria : criteria.add(workExample);
	}
	
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ boardList ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //

	
	
	
	
	
	
	
	
	
	
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ UPDATE ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	// load된 Work만 가지고 state 업데이트하기.
	@Transactional
	public void changeState(Work work, WorkState state) {
		currentSession()
		.createQuery("UPDATE Work w SET w.workState = :state, w.stateTime = now() WHERE w.id = :id")
		.setEntity("id", work)
		.setEntity("state", state)
		.executeUpdate();
	}
	// load된 Work만 가지고 workType 업데이트하기.
	@Transactional
	public void changeSWorkType(Work work, WorkType workType) {
		currentSession()
		.createQuery("UPDATE Work w SET w.workType = :workType WHERE w.id = :id").setEntity("id", work).setParameter("workType", workType)
		.executeUpdate();
	}	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ UPDATE ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	
	
	
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ Ajax를 위한 메서드 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	// Customer, Manager, Address등 업데이트하기
	@Transactional
	public int updateMember(Work work, String type, Object id) {
		return getDao().getSession().createSQLQuery("UPDATE " + getTableName() + " SET " + type + " = :id WHERE id = :work")
				.setParameter("id", nullValue(id))
				.setParameter("work", work)
				.executeUpdate();
	}
	
	// {address: 123, delivery: 1}
	public int updateDelivery(Work work, Map<String, Object> map) {
		Query sql = getDao().getSession()
				.createSQLQuery("UPDATE " + getTableName() + " SET address_id = :addressId, delivery = :deliveryId WHERE id = :work")
				.setParameter("work", work);
		
		//null property를 생략하는 setProperties를 우회하기 위한 루틴, null값을 집어넣기 위해서는 필요하다.
		// 참고로 null객체를 파라메터로 입력하면 null이 입력된다. 
		for(Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " / (" + entry.getValue() + ")");
			sql.setParameter(entry.getKey(), nullValue(entry.getValue()));
		}
		return sql.executeUpdate();
	}
	
	
	// 작업정보
	@Transactional(readOnly=true)
	public Work getWork(String id) {
		return (Work)getDao().criteria()
							.setFetchMode("subcontractor", FetchMode.JOIN)
							.setFetchMode("manager", FetchMode.JOIN)
							.add(Restrictions.idEq(id))
							.uniqueResult();
	}
	
	// 작업정보
	@Transactional(readOnly=true)
	public Work getWorkEditor(String id) {
		return (Work)getDao().criteria()
				.setFetchMode("subcontractor", FetchMode.JOIN)
				.setFetchMode("manager", FetchMode.JOIN)
				.add(Restrictions.idEq(id))
				.uniqueResult();
	}
	
	// 선택적인 패치
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ 작업 지울때 백업 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ // 
	@Override
	@Transactional
	public void delete(Work work) {
		currentSession().createQuery(getBackupQuery())
				.setParameter("id", work.getId()).executeUpdate();
		super.delete(work);
	}
	
	private String backupQuery; 
	private String getBackupQuery() {
		if(backupQuery == null) {
			backupQuery = "INSERT into WorkBackUp(" + getBackupColumnNames() + ") "
					+ "select " + getBackupColumnNames() + " FROM Work w WHERE w.id = :id";
		}
		return backupQuery;
	}
	
	// 백업 객체 컬럼명 가지고 오기
	private String backupColumns; 
	private String getBackupColumnNames() {
		if(backupColumns == null) {
			List<String> columns = new ArrayList<>();
			for(Method method : WorkBackUp.class.getDeclaredMethods()) {
				if(method.getAnnotation(Transient.class) == null && method.getName().startsWith("get")) {
					columns.add( org.springframework.util.StringUtils.uncapitalize(method.getName().substring(3)) );
				}
			}
			backupColumns = StringUtils.join(", ", columns);
		}
		return backupColumns;
	}
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ 작업 지울때 백업 ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ // 
	
	
}

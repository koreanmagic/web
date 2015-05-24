package kr.co.koreanmagic.service;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kr.co.koreanmagic.commons.KoDateUtils;
import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkBackUp;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.web.support.board.PagingQuery;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
	@Transactional(readOnly=true)
	public Object[] getStateList(WorkState state, boolean today, PagingQuery paging) {
		Criterion method = today ?
									Restrictions.and(Restrictions.eq("workState", state), Restrictions.lt("stateTime", new Date())) :
									Restrictions.eq("workState", state);
									
		return getWorkList(method, paging);
	}
	
	
	// 거래처 혹은 하청업체 해당 리스트
	@Transactional(readOnly=true)
	public Object[] getPartnerList(WorkState state, Partner partner, String name, PagingQuery paging) {
		return getWorkList(
						Restrictions.and(Restrictions.eq("workState", state), Restrictions.eq(name, partner)),
						paging
					);
	}
	
	
	@Transactional(readOnly=true)
	public Object[] getWorkList(Criterion method, PagingQuery paging) {
		return getWorkList(method, paging.getStart(), paging.getLimit(), paging.getOrder());
	}
	
	@Transactional(readOnly=true)
	public Object[] getWorkList(Criterion method, int start, int limit, String order) {
		
		Object[] result = new Object[2];
		
		// 총 갯수
		result[0] = (int)(long)getDao().criteria()
									.add(method)
									.setProjection(Projections.rowCount())
									.uniqueResult();
		// 리스트
		result[1] = getDao().getList(listCriteria().add(method), start, limit, order, null);
		
		return result; 
	}
	
	
	
	@Transactional(readOnly=true)
	public Criteria listCriteria() {
		return getDao().criteria()
				.setFetchMode("customer", FetchMode.JOIN)
				.setFetchMode("subcontractor", FetchMode.JOIN)
				.setFetchMode("manager", FetchMode.JOIN)
				;
	}
	
	
	// 기준 날짜에서 월 빼거나 더하기
	@Transactional(readOnly=true)
	public Object[] getListByMonth(WorkState state, LocalDateTime from, int months, PagingQuery paging) {
		LocalDateTime to = null;
		from = from == null ? LocalDateTime.now() : from;
		
		if(months < 0) {
			to = from;
			from = to.minusMonths( -(months));
		} else {
			to = from.plusMonths(months);
		}
		
		return getListByDate(state,
										KoDateUtils.localDateTimeToDate(from), 
										KoDateUtils.localDateTimeToDate(to), 
										paging
									);
	}
	
	// 기준 날짜에서 일수 빼거나 더하기
	@Transactional(readOnly=true)
	public Object[] getListByDays(WorkState state, LocalDateTime from, int days, PagingQuery paging) {
		LocalDateTime to = null;
		from = from == null ? LocalDateTime.now() : from;
		
		if(days < 0) {
			to = from;
			from = to.minusDays( -(days));
		} else {
			to = from.plusDays(days);
		}
		return getListByDate(state,
										KoDateUtils.localDateTimeToDate(from), 
										KoDateUtils.localDateTimeToDate(to), 
										paging
									);
	}
	
	public Object[] getListByDate(WorkState state, Date from, Date to, PagingQuery paging) {
		return getWorkList(Restrictions.between("insertTime", from, to), paging);
	}
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ boardList ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //

	
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ SEARCH ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@Transactional
	public List<Work> getStateTodayList(WorkState state) {
		return null;
	}
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ SEARCH ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	
	
	// 작업진행 현황
	@Transactional
	public List<Long> getStateCountByDate() {
		return getStateCountByDate(new Date());
	}
	
// eq-같다  bt-사이값  nb-사이값제외  ge-이상  le-이하  gt-초과  lt-미만  ne-같지않다 
	@Transactional
	public List<Long> getStateCountByDate(Date date) {
		return currentSession()
				.createSQLQuery("SELECT count(w.id) as count "
											+ "FROM work_state s LEFT JOIN hancome_work w "
											+ "ON w.state = s.id AND w.stateTime > :date "
											+ "GROUP BY s.id")
				.addScalar("count", LongType.INSTANCE)
				.setTimestamp("date", date )
				.list();
	}
	@Transactional
	public List<Long> getStateCount() {
		return currentSession()
				.createQuery("select count(w.id) from Work w group by w.workState")
				.list();
	}
	
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
					columns.add( StringUtils.uncapitalize(method.getName().substring(3)) );
				}
			}
			backupColumns = KoStringUtils.join(", ", columns);
		}
		return backupColumns;
	}
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ 작업 지울때 백업 ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ // 
	
	
}

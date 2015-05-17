package kr.co.koreanmagic.hibernate3;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;
import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.service.AddressService;
import kr.co.koreanmagic.service.BankNameService;
import kr.co.koreanmagic.service.BankService;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.service.GenericService;
import kr.co.koreanmagic.service.ManagerService;
import kr.co.koreanmagic.service.PartnerService;
import kr.co.koreanmagic.service.SubcontractorService;
import kr.co.koreanmagic.service.WorkResourceFileService;
import kr.co.koreanmagic.service.WorkService;
import kr.co.koreanmagic.service.support.WorkEditorService;
import kr.co.koreanmagic.web.support.board.PagingQuery;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.ManagedSessionContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;


@Configuration
@EnableAspectJAutoProxy
@EnableHibernate3
@ComponentScan({
	"kr.co.koreanmagic.dao",
	"kr.co.koreanmagic.service",
})
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("resource.properties")
@ContextConfiguration(classes={스프링_하이버네이트_테스트.class})
public class 스프링_하이버네이트_테스트 extends HibernateTestDao {

	@Autowired SessionFactory factory;
	
	@Autowired HibernateTransactionManager transactionManager;
	@Autowired List<? extends GenericService<?, ? extends Serializable>> serviceList;
	@Autowired CustomerService cs;
	@Autowired ManagerService ms;
	@Autowired WorkService ws;
	@Autowired AddressService as;
	@Autowired BankService bs;
	@Autowired BankNameService bns;
	@Autowired SubcontractorService sc;
	@Autowired WorkResourceFileService wrs;
	
	@Autowired Map<String, ? extends GenericService<?, ? extends Serializable>> ser;
	
	Session session;
	
	Customer customer = new Customer();
	Subcontractor sub = new Subcontractor();
	Manager manager = new Manager();
	Address address = new Address();
	Bank bank = new Bank();
	Work work = new Work();
	
	
	@Inject Provider<WorkEditorService> editor;
	/*
	 * 삭제 테스트
	 * 이미 세션에 컬렉션을 캐시된 상태라면, 컬렉션을 통해 지워야 한다.
	 * 하지만 컬렉션을 불러오지 않은 상태라면 객체 자신을 통해 지울 수 있다.
	 */
	@Test
	public void 삭제테스트() throws Exception {
		
		
		Object[] result = ws.getPartnerList(new WorkState(1), cs.load(212l), "customer", paging(1, 10, "id"));
		log("총갯수: " + result[0].getClass());
		log(result[1]);
	}
	
	private PagingQuery paging(int start, int limit, String order) {
		return new PagingQuery() {
			@Override
			public int getStart() {
				return start;
			}
			@Override
			public String getOrder() {
				return order;
			}
			@Override
			public int getLimit() {
				return limit;
			}
		};
	}
	
	public PartnerService<?> test() {
		return null;
	}
	
	//@Test
	public void 컨버세이션테스트() throws Exception {
		org.hibernate.classic.Session session = factory.openSession();
		session.setFlushMode(FlushMode.MANUAL);
		ManagedSessionContext.bind(session);
		
	}
	
	private void printStatus(TransactionStatus status) {
		log("hasSavepoint", status.hasSavepoint());
		log("isCompleted", status.isCompleted());
		log("isNewTransaction", status.isNewTransaction());
		log("isRollbackOnly", status.isRollbackOnly());
		transactionManager.rollback(status);
		log("hasSavepoint", status.hasSavepoint());
		log("isCompleted", status.isCompleted());
		log("isNewTransaction", status.isNewTransaction());
		log("isRollbackOnly", status.isRollbackOnly());
	}
	
	private void printValues(Object value) {
		log(joinObjectValues(value));
	}
	
	private void l(Iterable<Object> p ){
		
	}
	
	private String joinObjectValues(Object value) {
		
		if(value == null) return "null";
		if(!value.getClass().isArray()) return value.toString();
		
		Object[] values = (Object[])value;
		String result = "", separator = "  ";
		
		for(int i=0, len=values.length; i < len; i++) {
			
			if(values[i] == null)
				result += "null";
			
			else if(values[i].getClass().isArray())
				result += joinObjectValues(values[i]);
			
			else result += values[i].toString() + separator;
			
		}
		return result + "\n";
	}
	
	
	private void log(Object...obj) {
		String log = "", separater = " ";
		for(Object o : obj)
			log += o.toString() + separater;
		log(log.substring(0, log.length() - separater.length()));
	}

	@SuppressWarnings("unchecked")
	private<V extends GenericService<?, ? extends Serializable>, T, P extends Serializable> V findService(T entity) {
		for(GenericService<?, ? extends Serializable> service : serviceList) {
			if(service.getServiceClass().equals(entity))
				return (V)service;
		}
		throw new RuntimeException(entity + "에 해당하는 service가 없습니다.");
	}
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}

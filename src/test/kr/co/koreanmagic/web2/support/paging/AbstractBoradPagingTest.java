package kr.co.koreanmagic.web2.support.paging;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import kr.co.koreanmagic.dao.GenericDao;
import kr.co.koreanmagic.dao.WorkGroupDao;
import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkGroup;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.service.BankNameService;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.service.GenericService;
import kr.co.koreanmagic.service.ItemCategoryService;
import kr.co.koreanmagic.service.SubcontractorService;
import kr.co.koreanmagic.service.WorkGroupService;
import kr.co.koreanmagic.web.support.board.Paging;
import kr.co.koreanmagic.web2.support.CustomerBoardList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;



@Configuration
@EnableHibernate3
@ComponentScan({
	"kr.co.koreanmagic.dao",
	"kr.co.koreanmagic.service",
	"kr.co.koreanmagic.web2.support",
})
@EnableTransactionManagement(proxyTargetClass=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AbstractBoradPagingTest.class})
public class AbstractBoradPagingTest {

	@Autowired List<? extends GenericDao<?, ? extends Serializable>> daoList;
	@Autowired List<? extends GenericService<?, ? extends Serializable>> serviceList;
	
	@Autowired SessionFactory factory;
	
	@Inject Provider<CustomerBoardList> list;
	@Autowired BankNameService service;
	@Autowired(required=false) GenericConversionService conversionService;
	
	String name;
	
	@Test
	@Transactional
	@Rollback(false)
	public void test() throws Exception {

		log(service.getDao());
		
	}
	
	private void 워크입력() {
		WorkGroupDao dao = findDao(WorkGroup.class);
		WorkGroupService service = findService(WorkGroup.class);
		ItemCategoryService items = findService(ItemCategory.class);
		CustomerService customerService = findService(Customer.class);
		SubcontractorService subService = findService(Subcontractor.class);
		
		
		ItemCategory category = items.get(1l);
		Customer customer = customerService.get(500l);
		Subcontractor subcontractor = subService.get(120l);

		WorkGroup workGroup = service.getInitalBean();
		workGroup.setSubject("테스트 워크그룹");
		workGroup.setCustomer(customer);
		
		Work work = null;
		for(int i = 0; i < 5; i++) {
			work = new Work();
			work.setSubconstractor(subcontractor);
			work.setMemo("테스트 워크" + i);
			workGroup.addWork(work);
		}
		
		log(service.add(workGroup));
	}
	
	class FackCustomer {
		Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
	}
	

	
	@SuppressWarnings("unchecked")
	private<V extends GenericService<?, ? extends Serializable>, T, P extends Serializable> V findService(T entity) {
		for(GenericService<?, ? extends Serializable> service : serviceList) {
			if(service.getServiceClass().equals(entity))
				return (V)service;
		}
		throw new RuntimeException(entity + "에 해당하는 service가 없습니다.");
	}
	
	@SuppressWarnings("unchecked")
	private<V extends GenericDao<?, ? extends Serializable>, T, P extends Serializable> V findDao(T entity) {
		for(GenericDao<?, ? extends Serializable> dao : daoList) {
			if(dao.getPersistentClass().equals(entity))
				return (V)dao;
		}
		throw new RuntimeException(entity + "에 해당하는 dao가 없습니다.");
	}
	
	
	private void nativeQuery(Session session) {
		session.doWork(con -> {
			PreparedStatement ps = con.prepareStatement("UPDATE testTable SET name='"+ name + "' WHERE id = ?");
			ps.setLong(1, 56l);
			ps.execute();
		});
	}
	private void query(Session session) {
		Query query = session.createQuery("UPDATE TestTable SET name='"+ name + "' WHERE id = 56");
		query.executeUpdate();
	}
	private void sqlQeury(Session session) {
		Query query = session.createSQLQuery("UPDATE testTable SET name='"+ name + "' WHERE id = 56");
		query.executeUpdate();
	}
	
	class A {
		String get() { return getClass().getSimpleName(); }
		<T extends A> String get(T t) {
			return t.get();
		}
	}
	
	class B extends A {
		String get() { return getClass().getSimpleName(); }
	}
	
	class C extends B {
		String get() { return getClass().getSimpleName(); }
	}

	
	
	private String printPage(Paging boardPaging) {
		String result = "";
		for(int s : boardPaging.getBeforePage())
			result += s + " ";
		result += "[" + boardPaging.getCurrentPage() + "] ";
		for(int e : boardPaging.getAfterPage())
			result += e + " ";
		return result.substring(0, result.length()-1);
	}
	
	private static void log(Object obj) {
		System.out.println(obj);
	}
	
	
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ConversionServiceFactoryBean conversionService() {
		ConversionServiceFactoryBean factory =  new ConversionServiceFactoryBean();
		return factory;
	}

	
}

package kr.co.koreanmagic.hibernate3;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import kr.co.koreanmagic.dao.GenericDao;
import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkGroup;
import kr.co.koreanmagic.service.GenericService;
import kr.co.koreanmagic.service.WorkGroupService;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;




@Configuration
@EnableAspectJAutoProxy
@EnableHibernate3
@ComponentScan({
	"kr.co.koreanmagic.dao",
	"kr.co.koreanmagic.service",
})
@EnableTransactionManagement(proxyTargetClass=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={스프링_하이버네이트_테스트.class})
public class 스프링_하이버네이트_테스트 extends HibernateTestDao {

	@Autowired SessionFactory factory;
	
	@Autowired List<? extends GenericDao<?, ? extends Serializable>> daoList;
	@Autowired List<? extends GenericService<?, ? extends Serializable>> serviceList;
	
	static Connection con;
	
	@Test
	@Transactional
	public void test() throws Exception {
		
		WorkGroupService gSrvice = findService(WorkGroup.class);
		
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
	
	@SuppressWarnings("unchecked")
	private<V extends GenericDao<?, ? extends Serializable>, T, P extends Serializable> V findDao(T entity) {
		for(GenericDao<?, ? extends Serializable> dao : daoList) {
			if(dao.getPersistentClass().equals(entity))
				return (V)dao;
		}
		throw new RuntimeException(entity + "에 해당하는 dao가 없습니다.");
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}

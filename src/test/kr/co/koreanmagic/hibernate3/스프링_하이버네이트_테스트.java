package kr.co.koreanmagic.hibernate3;

import kr.co.koreanmagic.hibernate3.config.SpringConfiguration;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.test.TestTable;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringConfiguration.class})
public class 스프링_하이버네이트_테스트 extends HibernateTestDao {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired SessionFactory sessionFactory;
	@Autowired PlatformTransactionManager transactionManager;
	
	private TransactionTemplate transactionTemplate;
	
	@Test
	public void test() {
		
		
		get(Customer.class, 1l);

		/*TransactionDefinition definition = null;
		Session session = null;
		TransactionStatus status = null;
		
		definition = new DefaultTransactionDefinition();
		status = transactionManager.getTransaction(definition);
		
		session = sessionFactory.getCurrentSession();
		session.save(new TestTable("원래"));
		printValues(session.createSQLQuery("SELECT * FROM testTable LIMIT 0,1").uniqueResult());
		transaction1(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED), session);
		
		transactionManager.rollback(status);*/
	}
	
	private void transaction1(TransactionDefinition definition, Session otherSession) {
		Session session = null;
		TransactionStatus status = transactionManager.getTransaction(definition);
		session = sessionFactory.getCurrentSession();
		printValues(session.createSQLQuery("SELECT * FROM testTable LIMIT 0,1").uniqueResult());
		session.save(new TestTable("새로운 트랜잭션"));
		same(session, otherSession);
		equals(session, otherSession);
		transactionManager.commit(status);
	}
	
	
	private void printValues(Object value) {
		log(joinObjectValues(value));
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

}

package kr.co.koreanmagic.db.mybatis;

import javax.sql.DataSource;

import kr.co.koreanmagic.web.db.mybatis.config.ConfigureMyBatis;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkMapper;
import kr.co.koreanmagic.web.domain.Work;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ConfigureMyBatis.class})
public class 트랜잭션테스트 {

	@Autowired DataSource dataSource;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired WorkMapper workMapper;
	
	Logger logger = Logger.getLogger(getClass());
	
	@Test
	public void 트랜잭션동기화() throws Exception {
		
		//TransactionSynchronizationManager.initSynchronization();
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		workMapper.insertSelective(getWork("status"));
		TransactionStatus status2 = this.transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		
		workMapper.insertSelective(getWork("status2"));
		
		out(status.toString());
		out("status isCompleted? : " + status.isCompleted());
		this.transactionManager.commit(status);
		status.flush();
		this.transactionManager.rollback(status2);
		out(status2.toString());
		
	}
	public Work getWork(String item) {
		return new Work();
	}

	private static void out(Object obj) {
		System.out.println(obj);
	}
}

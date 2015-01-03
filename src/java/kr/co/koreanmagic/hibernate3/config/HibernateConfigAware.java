package kr.co.koreanmagic.hibernate3.config;

import java.util.Map;

import javax.sql.DataSource;

import kr.co.koreanmagic.context.support.convert.IntegerToWorkStateConverter;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

@Configuration
public class HibernateConfigAware implements ImportAware {
	
	private Logger logger = Logger.getLogger(HibernateConfigAware.class);
	
	@Bean
	public AnnotationSessionFactoryBean sessionFactory(DataSource dataSource) {
		AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();

		sessionFactory.setDataSource(dataSource);
		
		Resource resource = new ClassPathResource("hibernate.cfg.xml");
		
		sessionFactory.setPackagesToScan("kr.co.koreanmagic.hibernate3.mapper.*");
		sessionFactory.setConfigLocation(resource);
		return sessionFactory;
	}
	
	
	
	
	
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> elements = importMetadata.getAnnotationAttributes(EnableHibernate3.class.getName());
	}
	
}
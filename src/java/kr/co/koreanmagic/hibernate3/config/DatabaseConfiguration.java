package kr.co.koreanmagic.hibernate3.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("database.properties")
public class DatabaseConfiguration {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Bean
	public DataSource dataSource(@Value("${database.driverClass}") String driverClassName,
								@Value("${database.url}") String url,
								@Value("${database.username}") String username,
								@Value("${database.password}") String password
								) {
		
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		return dataSource;
	}


}

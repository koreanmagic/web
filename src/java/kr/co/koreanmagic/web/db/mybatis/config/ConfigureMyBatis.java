package kr.co.koreanmagic.web.db.mybatis.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/*
 * MyBatis를 구동하게 하는 설정파일
 * 흔히 사용하는 mybatis-config.xml의 설정을 최소화()
 */
@Configuration
@PropertySource("database.properties") // 

/*
 * 매퍼인터페이스를 등록하는 스캐너
 * 스캐너는 매퍼인터페이스를 스프링 빈으로도 등록시켜준다. mapper를 주입받아서 그대로 사용할수도 있다. 
 */

@MapperScan("kr.co.koreanmagic.web.db.mybatis.mapper")
@ComponentScan({"kr.co.koreanmagic.web.db.mybatis.support"})
public class ConfigureMyBatis {


	Logger logger = Logger.getLogger(getClass());
	
	@Value("${mybatis.mapper.location}") String mapperRootPath;
	
	
	// DataSource
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

	
	//@Autowired TypeHandler<?>[] typeHandlers;
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,
														@Value("${mybatis.typeAliasesPackage}") String typeAliasesPackage,
														@Value("${mybatis.mybatis-config.location}") Resource configLocation,
														@Value("${mybatis.mapper.location}") final String mapperRootPath
														) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		logger.debug(bean);
		//bean.setTypeHandlers(typeHandlers);
		bean.setTypeAliasesPackage(typeAliasesPackage); // 접두사
		bean.setConfigLocation(configLocation); // mybatis의 대표설정파일
		bean.setDataSource(dataSource); // 커넥션을 가져오기 위한 데이터베이스 정보
		Resource[] resources = new Resource[]{
//				new ClassPathResource(mapperRootPath + "WorkMapper.xml"),
				new ClassPathResource(mapperRootPath + "WorkConditionMapper.xml"),
//				new ClassPathResource(mapperRootPath + "WorkFileMapper.xml"),
//				new ClassPathResource(mapperRootPath + "WorkListTypeMapper.xml"),
//				new ClassPathResource(mapperRootPath + "WorkResourceMapper.xml"),
			};
		try {
			bean.setMapperLocations(resources);
		} catch (Exception e) {
			logger.debug("Error");
			e.printStackTrace();
		}
		return bean;
	}
	
	
	// 트랜잭션 매니저
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	
	/*
	SqlSessionTemplate은 마이바티스 스프링 연동모듈의 핵심이다. SqlSessionTemplate은 SqlSession을 구현하고 코드에서 SqlSession를 대체하는 역할을 한다.
	SqlSessionTemplate 은 쓰레드에 안전하고 여러개의 DAO나 매퍼에서 공유할수 있다.
	getMapper()에 의해 리턴된 매퍼가 가진 메서드를 포함해서 SQL을 처리하는 마이바티스 메서드를 호출할때 SqlSessionTemplate은 SqlSession이
	현재의 스프링 트랜잭션에서 사용될수 있도록 보장한다. 추가적으로 SqlSessionTemplate은 필요한 시점에 세션을 닫고, 커밋하거나 롤백하는 것을
	포함한 세션의 생명주기를 관리한다. 또한 마이바티스 예외를 스프링의 DataAccessException로 변환하는 작업또한 처리한다.

	SqlSessionTemplate은 마이바티스의 디폴트 구현체인 DefaultSqlSession 대신 항상 사용된다.
	왜냐하면 템플릿은 스프링 트랜잭션의 일부처럼 사용될 수 있고 여러개 주입된 매퍼 클래스에 의해 사용되도록 쓰레드에 안전하다.
	동일한 애플리케이션에서 두개의 클래스간의 전환은 데이터 무결성 이슈를 야기할수 있다.
	*/
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	
	
	/*
	 * Mapper 클래스를 만들어 뿌려주는 MapperFactoryBean
	 * 사실 이 클래스는 사용할 필요가 없는 것 같다.
	 * @MapperScan만 사용해도 스프링이 레지스터에 등록되는 모든 Mapper를 싱글톤 빈으로 등록하는 듯 싶다.
	 * 한마디로 각 서비스 객체에서는 @Autowired로 받기만 하면 된다는 뜻. 
	 * 이 MapperFactoryBean은 각 Mapper별로 하나씩 만들어야 하는데, 그런 점에서 스프링이 해주는 역할은 아주 효율적이라고 할 수 있겠다. 
	 * 
	 */
	//@Bean
	/*@Deprecated
	public MapperFactoryBean<MySiteMapper> mapperFactoryBean(SqlSessionFactory sqlSessionFactory) {
		MapperFactoryBean<MySiteMapper> mfb = new MapperFactoryBean<>();
		mfb.setMapperInterface(MySiteMapper.class);
		mfb.setSqlSessionFactory(sqlSessionFactory);
		return mfb;
	}*/
	
	
	/*
	 *  마이바티스 매퍼인터페이스를 자동으로 검색해서 등록시키는 스프링연동 클래스
	 *  static으로 선언해야 한다. 하지만 @ScanMapper로 대체
	 */
	//@Bean
	@Deprecated
	public static MapperScannerConfigurer  mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("kr.co.koreanmagic.db.mybatis.mapper");

		return configurer;
	}
	
	
	
	/*
	 * 모든 프로퍼티 key value를 통합해서 사용할 수 있도록 해주는 객체
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	
	
	
}

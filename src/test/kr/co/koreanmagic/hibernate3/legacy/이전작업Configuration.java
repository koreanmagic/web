package kr.co.koreanmagic.hibernate3.legacy;

import java.util.Set;

import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableHibernate3
@ComponentScan({
	"kr.co.koreanmagic.dao",
	"kr.co.koreanmagic.service",
	"kr.co.koreanmagic.hibernate3.legacy",
	"kr.co.koreanmagic.context.support.convert",
})
@EnableTransactionManagement(proxyTargetClass=true)
public class 이전작업Configuration {
	
	@Autowired Set<Converter<?,?>> converters;
	
	@Bean
	public ConversionServiceFactoryBean conversionService() {
		ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
		
		factory.setConverters(converters);
		
		return factory;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}

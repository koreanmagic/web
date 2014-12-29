package kr.co.koreanmagic.web2.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kr.co.koreanmagic.commons.KoUtils;
import kr.co.koreanmagic.context.support.convert.IntegerToWorkStateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@Configuration
public class RegisterInfraBeans {
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 인프라 빈 등록 ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	/*
	 * 프로퍼티 소스를 하나로 통합해주는 플래스홀더
	 * @Autowired Environment env; env.getProperty("key")를 통해서도 사용할 수 있다.
	 * 
	 * ★프로퍼티 소스의 value를 ,로 구분해 적으면 String[]로 각각의 값을 받을 수 있다.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	

}

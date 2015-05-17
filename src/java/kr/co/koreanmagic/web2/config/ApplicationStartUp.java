package kr.co.koreanmagic.web2.config;

import java.nio.charset.Charset;
import java.util.List;

import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;
import kr.co.koreanmagic.web2.config.view.ViewResolverConfiguration;
import kr.co.koreanmagic.web2.servlet.interceptor.HandlerInterceptorRegister;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@EnableHibernate3
@Import(value={RegisterInfraBeans.class, ViewResolverConfiguration.class})
@ComponentScan(value={
		"kr.co.koreanmagic.web2",
		"kr.co.koreanmagic.dao",
		"kr.co.koreanmagic.service",
		"kr.co.koreanmagic.context.support.convert",
})
public class ApplicationStartUp extends WebMvcConfigurerAdapter {

	
	private Logger logger = Logger.getLogger(getClass());
	
	
	@Autowired List<Converter<?, ?>> converters;
	@Override
	public void addFormatters(FormatterRegistry registry) {
		for(Converter<?,?> converter : converters) {
			registry.addConverter(converter);
		}
		super.addFormatters(registry);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		converters.add(new MappingJacksonHttpMessageConverter());	// JSON 변환
	}

	@Override
	public Validator getValidator() {
		return super.getValidator();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		super.configureContentNegotiation(configurer);
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		super.configureAsyncSupport(configurer);
	}

	@Autowired
	private List<HandlerMethodArgumentResolver> myArgumentResolvers;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(myArgumentResolvers);
		super.addArgumentResolvers(argumentResolvers);
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		super.addReturnValueHandlers(returnValueHandlers);
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		super.configureHandlerExceptionResolvers(exceptionResolvers);
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return super.getMessageCodesResolver();
	}

	@Autowired HandlerInterceptorRegister[] interceptors;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration;
		
		for(HandlerInterceptorRegister i : interceptors) {
			registration = registry.addInterceptor(i);
			if(i.getPathPatterns() != null && i.getPathPatterns().length > 0)
				registration.addPathPatterns(i.getPathPatterns());
		}
		super.addInterceptors(registry);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
	}

	
	/*
	 * 리소스 핸들러
	 */
	@Value("${hancome.workfile.root}") String resourcePath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println(resourcePath);
		registry.addResourceHandler("/resource/**").addResourceLocations( "file:" + resourcePath + "/" );
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 서블릿 관련 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	/********  <mvc:default-servlet-handler />  ********/
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
		super.configureDefaultServletHandling(configurer);
	}
	
	/* ▩▩▩ 멀티파트 리졸버 ▩▩▩ */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(Integer.MAX_VALUE); // 최대업로드 가능한 바이트크기
		resolver.setMaxInMemorySize(8192*1024*10); // 디스크에 임시 파일을 생성하기 전에 메모리에 보관할수있는 최대 바이트 크기
		resolver.setDefaultEncoding("utf8"); // 요청을 파싱할 때 사용할 캐릭터 인코딩. 기본값 ISO-8859-1
		return resolver;
	}
	

}

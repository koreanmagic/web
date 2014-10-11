package kr.co.koreanmagic.web.config.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.cache.TemplateLoader;
import freemarker.template.utility.XmlEscape;


@Configuration
@PropertySource("freemarker.properties")
public class FreeMarkerConfiguration implements ImportAware {

	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired FreeMarkerViewResolver resolver;
	
	/* ▩▩▩ 리졸버 프리마커 ▩▩▩ */
	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver(
				@Value("${freemarker.order}") int order, // 템플릿 파일 위치
				@Value("${freemarker.cache}") boolean cache,
				@Value("${freemarker.suffix}") String suffix,
				@Value("${freemarker.contentType}") String contentType
			) {
		
		FreeMarkerViewResolver resolver = new T();
		resolver.setOrder(order);
		resolver.setCache(cache);
		resolver.setSuffix(suffix);
		resolver.setContentType(contentType);
		resolver.setExposeSpringMacroHelpers(true); // 스프링 매크로 사용할 수 있게!!
		
		return resolver;
	}
	static class T extends FreeMarkerViewResolver {
		@Override
		public View resolveViewName(String viewName, Locale locale) throws Exception {
			return super.resolveViewName(viewName, locale);
		}
				
	}
	
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer(
				@Value("${freemarker.templateLoaderPath}") String[] templateLoaderPaths, // 템플릿 파일 위치
				@Value("${freemarker.defaultEncoding}") String defaultEncoding
			) {
		
		Properties props = new Properties();
	    props.setProperty("number_format","0.##");
	    //props.setProperty("locale","en-GB");
	    freemarker.template.Configuration con = new freemarker.template.Configuration();
	    
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPaths(templateLoaderPaths);
		configurer.setDefaultEncoding(defaultEncoding);

		// escape 유틸리티 등록
		Map<String, Object> variables = new HashMap<>();
		variables.put("xml_escape", new XmlEscape());
		configurer.setFreemarkerVariables(variables);
		
		return configurer;
	}


	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableFreeMarker.class.getName());
		resolver.setOrder((int)map.get("order"));
	}
	
}

package kr.co.koreanmagic.web.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.sql.DataSource;

import kr.co.koreanmagic.web.config.view.EnableFreeMarker;
import kr.co.koreanmagic.web.controller.interceptor.HandlerInterceptorRegister;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.servlet.view.tiles2.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;



/*
 *	@EnableWebMvc는 기본적으로 Spring @MVC를 가능하게 하는 여러가지 인프라 빈을 등록한다.
 *	또한 이를 설정하는 과정에서 사용자가 특정 설정정보들을 커스터마이징하도록 확장포인트를 열어놓았다.
 *	이는 WebMvcConfigurer 인터페이스를 통해 구현하도록 했으며 WebMvcConfigurerAdapter는 이 인터페이스를
 *	구현한 일종의 껍데기 클래스다. 인터페이스에 강제된 메소드를 구현하느라 불필요하게 코드가 늘어나는 것을 방지해준다. 
 */
//@ImportResource({"classpath:/hancome-servlet.xml"}) // 여기에서의 /는 폴더구조상의 루트폴더이다.

@Configuration
@EnableWebMvc
@EnableFreeMarker
@ComponentScan({"kr.co.koreanmagic.web.controller", // 컨트롤러와 그 이하 패키지 컴포넌트들
				"kr.co.koreanmagic.web.support",  // 포매터 및 컨버터를 빈으로 등록
				"kr.co.koreanmagic.web.service"  // daoService 빈으로 등록
				})
@PropertySource("web-resource.properties") // 각종 웹용 리소스 정보
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired HandlerInterceptorRegister[] intercepters;
	
	
	
	
	/* ▩▩▩ 인터셉터 ▩▩▩ */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration;
		for(HandlerInterceptorRegister i : intercepters) {
			registration = registry.addInterceptor(i);
			if(i.getPathPatterns() != null)
				registration.addPathPatterns(i.getPathPatterns());
			logger.debug(i.getClass().getSimpleName());
		}
	}
	
	private @Value("${Work.file.rootpath}") String root;
	/* ▩▩▩ 리소스 ▩▩▩ */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		final String file = "file:";
		String workResourcePath = file + root;
		logger.debug("Work Resource RequestPath : " + workResourcePath);
		registry.addResourceHandler("/work/**")
				.addResourceLocations(workResourcePath);
	}
	

	@Autowired
	private List<HandlerMethodArgumentResolver> myArgumentResolvers;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(myArgumentResolvers);
		super.addArgumentResolvers(argumentResolvers);
	}




	/* ******* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 메시지 컨버터 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ******* */
	/*
	 * @RequestBody @ResponseBody의 메세지를 컨버팅해주는 역할
	 * AnnotationMethodHandlerAdapter에는 HttpMessageConverter 타입의 메시지 변환기가 여러개 등록되어 있다.
	 * @RequestBody가 붙은 파라미터가 있으면, 들어온 HTTP 요청의 미디어타입과 파라미터의 타입을 먼저 확인한다.
	 * 메시지 변환기 중에서 해당 미디어 타입을 처리할 수 잇는 것이 있다면, 이를 통해 본문을 통째로 변환해서 파라미터로 넣어준다.
	 * @ResponseBody는 이 어노테이션이 붙은 메서드가 리턴하는 오브젝트를 HttpServletResponse의 출력스트림으로 바로 써버린다.
	 */
	/* ▨▨▨▨▨ Message Converter ▧▧▧▧▧ */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 디폴트 메시지 컨버터
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new FormHttpMessageConverter());
		converters.add(new Jaxb2RootElementHttpMessageConverter());
		
		// JSON 메시지 컨버터 
		converters.add(new MappingJacksonHttpMessageConverter());
		
	}
	
	
	/* ▩▩▩ 정적자원 매핑 ▩▩▩ */
	/*
	 * URL 주소와 정적 자원을 그대로 매핑한다.
	 * 관례를 이용하는 듯 하다.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		List<String> list = new ArrayList<>();
		list.add("고");
		list.add("정");
		list.add("철");
		Stream<String> stream = list.stream();
		stream.forEach(x -> logger.debug("addViewControllers : " + x));
	}
	
	
	
	
	
	/* ******* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 리졸버 설정 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ******* */
	/*
	 * ① URL path를 통해 미디어 타입을 결정해 놓는다. ex) koreanmagic.co.kr/text.html  --> html
	 * ② 등록된 뷰 리졸버들에게 path를 던져서 모든 뷰를 받아놓는다.
	 * ③ 돌려받은 뷰의 getContentType()를 호출해서 ①에서 결정된 미디어타입과 비교해본다.
	 * 	 ☞ 이때 반환되는 String은 컨텐츠타입이다. 즉 text/html 식의 문장이다.
	 *     이를 비교하기 위해서 미디어타입을 미리 등록해놔야 한다. ex) (html, "text.html")
	 * 비교해서 getContentType()와 미디어타입이 일치하면 View가 결정된다.
	 */
	
	/* ▩▩▩ ContentNegotiatingViewResolver  교섭 리졸브 ▩▩▩ */
	@Bean
	public ContentNegotiatingViewResolver viewResolver() {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();

		// 미디어타입 등록
		Map<String, String> mediaTypes = new HashMap<>();
		mediaTypes.put("atom", "application/atom+xml");
		mediaTypes.put("html", "text/html");
		mediaTypes.put("json", "application/json");
		resolver.setMediaTypes(mediaTypes);
		
		resolver.setDefaultViews(Arrays.asList(new View[]{jsonView()}));
		
		/* 교섭리졸버는 아래 등록되는 모든 뷰리졸버들에게 View를 요청한다.
		 * 이렇게 반환된 View 중에서 ContentType이 동일한 View를 선택한다.
		 * 둥록되는 순서가 우선순위가 된다.
		 */
		/*resolver.setViewResolvers(Arrays.asList(
				new ViewResolver[]{ 
						//tilesViewResolver(), // 타일즈 리졸버
						freeMarkerViewResolver, // 프리마커
						//velocityViewResolver, // 벨로시티
						//beanNameViewResolver(), // 빈 네임
						//internalResourceViewResolver(), // JSP 용
						}
				));*/
		return resolver;
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
	
	
	
	/* ▩▩▩ 리졸버 InternalResourceViewResolver ▩▩▩ */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver irvr = new InternalResourceViewResolver();
		irvr.setOrder(10);
		irvr.setPrefix("/WEB-INF/view/");
		irvr.setSuffix(".jsp");
		return irvr;
	}
	
	/* ▩▩▩ 리졸버 BeanNameViewResolver ▩▩▩ */
	@Bean
	public BeanNameViewResolver beanNameViewResolver() {
		// 빈 이름으로 검색
		return new BeanNameViewResolver();
	}
	
	
	
	
	
	
	/* ▩▩▩ 리졸버 타일즈 ▩▩▩ */
	/*
	 * ★★★★ 타일즈2 에러 ★★★★
	 * 타일즈2는 전용로거로 jcl-over-slf4j-1.5.8.jar을 사용한다.
	 * 근데 이게 log4g 1.5.6과 충돌을 일으키는건지, 아주 요상한 존나 이상한 오작동을 보인다. (이거때매 멘붕겪음)
	 * 오작동의 유형은 어떤 URL을 입력하든 백지 페이지가 응답된다. debug등의 메세지도 완전히 소멸된다.
	 * 이를 해결하는 방법은 dependencyManagement를 이용해
	 * jcl-over-slf4j-1.6.4.jar, slf4j-api.1.6.4, slf4j-log4j12.1.6.4를 사용하는 것이다.
	 */
	@Bean
	public UrlBasedViewResolver tilesViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setViewClass(TilesView.class);
		return resolver;
	}
	
	/*@Bean
	public TilesViewResolver tilesViewResolver() {
		TilesViewResolver resolver = new TilesViewResolver();
		resolver.setViewClass(TilesView.class);
		resolver.setOrder(1);
		return resolver;
	}*/
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer conf = new TilesConfigurer();
		conf.setDefinitions(new String[]{"/WEB-INF/tiles/*.xml"}); // 타일즈 설정파일
		//conf.setCompleteAutoload(true); // EL 표현식과 와일드카드를 이용해 설정파일을 컨트롤하는 기능  ★★이거만 키면 동작이 안됨★★
		conf.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
		return conf;
	}
	
	
	
	/* ******* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 뷰 View 설정 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ******* */
	@Bean
	public MappingJacksonJsonView jsonView() {
		return new MappingJacksonJsonView();
	}
	
	
	/* ******* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 포매터 등록 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ******* */
	/*
	 * kr.co.koreanmagic.support 이하에 AnnotationFormatterFactory를 구현한 포매터를 등록해놓으면
	 * ① @ComponentScan에 의해 자동으로 빈 등록이 되고, 그렇게 등록된 빈은
	 * ② @Autowired에 의해 배열로 필드에 주입된다.
	 * ③ 이 필드를 이용해 addFormatters 메소드를 통해 Formatter를 등록한다.  
	 */
	
	/* 등록된 모든 AnnotationFormatterFactory를 List로 받는다. */
	@Autowired(required = false)
	private AnnotationFormatterFactory<? extends Annotation>[] formatters;
		
	
	@Autowired List<Converter<?, ?>> converters;
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		if(formatters != null) {
			for (AnnotationFormatterFactory<? extends Annotation> formatter : formatters) {
				registry.addFormatterForFieldAnnotation(formatter);
			}
		}
		for(Converter<?, ?> c : converters) {
			registry.addConverter(c);
		}
		
	}
	

	
	/* ******* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 서블릿 관련 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ******* */
	
	/********  <mvc:default-servlet-handler />  ********/
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
		super.configureDefaultServletHandling(configurer);
	}
	
	
	
	
	
	/******** ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▼ 빈 검증 ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ********/
	
	/*
	 * 바인딩 작업시 에러가 있을때 나타내줄 에러메세지 프로퍼티 관리자.
	 * 이 클래스는 디폴드로 등록되지 않는다. 따라서 바인딩 로직을 짤때는 수동으로 등록해주어야 한다.
	 * 
	 * ★★★★ 이 클래스의 bean name은 반드시 messageSource로 해야한다. ★★★★
	 * ??아마도 다른 바인딩 객체에서 @Resource("messageSource")라고 하는 듯  
	 */
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
		rbms.setBasename("messages");
		return rbms;
	}
	
	/*
	 * JSR-303빈 검증방식.
	 * LocalValidatorFactoryBean는 일종의 어댑터 클래스.
	 * LocalValidatorFactoryBean을 빈으로 등록하면 
	 * ① 컨트롤러에서 Validator타입으로 DI받아서 @InitBinder에서 WebDAtaBinder에 설정하거나
	 * ② 코드에서 직접 Validator처럼 사용할 수 있다.
	 * 
	 * JSR-303 검증방식을 사용하려면 LocalValidatorFactoryBean을 생성해서 WebDataBinder에 항시 등록해줘야 한다.
	 * ★★★ 이를 일괄자동화하기 위해서는 ConfigurableWebBindingInitializer에 LocalValidatorFactoryBean를 셋팅후
	 * ConfigurableWebBindingInitializer를 AnnotationMethodHandlerAdapter에 등록해야한다. ★★★
	 * 
	 * 스프링3.1 이상부터는 JSR-303빈 검증이 디폴트로 된 듯 싶다.
	 * 아래와 같은 설정을 하지 않아도 JSR-303 검증이 정상 작동한다.
	 * 
	 */
	/*@Bean
	public LocalValidatorFactoryBean localValidator() {
		return new LocalValidatorFactoryBean();
	}
	@Bean
	public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter() {
		AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
		adapter.setWebBindingInitializer(bindingInitializer());
		return adapter;
	}
	@Bean
	public ConfigurableWebBindingInitializer bindingInitializer() {
		ConfigurableWebBindingInitializer init = new ConfigurableWebBindingInitializer();
		init.setValidator(localValidator());
		return init;
	}*/
	
	
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

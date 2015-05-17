package kr.co.koreanmagic.web2.config.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.tiles3.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@PropertySource("freemarker.properties")
public class ViewResolverConfiguration {
	
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
		
		//resolver.setDefaultViews(Arrays.asList(new View[]{jsonView()}));
		
		/* 교섭리졸버는 아래 등록되는 모든 뷰리졸버들에게 View를 요청한다.
		 * 이렇게 반환된 View 중에서 ContentType이 동일한 View를 선택한다.
		 * 둥록되는 순서가 우선순위가 된다.
		 * 뷰리졸버를 여기에 등록하지 않으면 동작하지 않는다. 
		 */
		resolver.setViewResolvers(Arrays.asList(
				new ViewResolver[]{ 
						tilesViewResolver(), // 타일즈 리졸버
						//customerViewResolver(), // 프리마커
						//velocityViewResolver, // 벨로시티
						//beanNameViewResolver(), // 빈 네임
						//internalResourceViewResolver(), // JSP 용
						//customerViewResolver()
						}
				));
		return resolver;
	}
	
	
	/* 
	 * 타일즈3
	 * 타일즈 설정파일에 templateType="freemarker"만 해줘도 알아서 프리마커를 렌더링한다. 대박!!
	 * 프리마커 설정은 따로 필요 없다.ㅠㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ
	 * 
	 * 타일즈를 사용할때 Freemarker Resolver를 사용할 수 없는 이유.(추정)
	 * 타일즈는 Taglib을 통해 직접 서블릿을 호출하는 구조같다.
	 * 때문에 콘트롤러에서 돌려주는 viewName으로 View를 찾는 ViewResolver를 이용하지 않는다.
	 * 이같은 연유로 Freemarker Resolver를 등록해봐야 타일즈에서 렌더링할때 Resolver를 호출하지 않는것이다.
	 * 
	 */
	public TilesViewResolver tilesViewResolver() { 
		TilesViewResolver resolver = new TilesViewResolver();
		resolver.setOrder(0);
		resolver.setContentType("text/html; charset=UTF-8");
		resolver.setCache(true);
		resolver.setPrefix("tiles3.");
		return resolver;
	}
	
	
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
	    
		TilesConfigurer config = new TilesConfigurer();
		config.setDefinitions("/WEB-INF/tiles*.xml");
		/*
		 * 	2014-10-26 (pm6 ~ pm9) 3시간동안 씨름하면서 소스분석 결과..ㅠ
		 * 
		 * 	config.setCompleteAutoload(boolean) 설정에 관하여..
		 * 
		 *	TilesConfigurer의 afterPropertiesSet() 메서드가 Tiles3의 초기화를 진행한다.
		 *	afterPropertiesSet()는 내부변수인 TilesInitializer의 initialize()를 호출해 초기화 루틴을 실행시킨다.
		 *		
		 *	★true일 경우
		 *	CompleteAutoloadTilesInitializer 상속의 SpringCompleteAutoloadTilesInitializer와
		 *	CompleteAutoloadTilesContainerFactory 상속의 SpringCompleteAutoloadTilesContainerFactory가 발동
		 *	:: freemarker, vellocity 등의 모든 Renderer가 기본값으로 초기화되며 Tiles3가 권장하는 모든 기본값으로 설정된다.
		 *	:: tiles.xml에서 templateType 어트리뷰트를 사용할 수 있다.
		 *	
		 *	★false일 경우
		 *	DefaultTilesInitializer 상속의 TilesInitializer와
		 *	BasicTilesContainerFactory 상속의 SpringTilesContainerFactory가 발동
		 *	:: Renderer와 Difinition등을 직접 등록해야 한다.
		 *	
		 *	true로 해놓고 쓸 경우 문제점은 기본 Renderer의 default_encoding이 ISO-8859-1라는 점이다.
		 *	default_encoding은 저장된 파일을 IO로 읽어들일때 사용하는 encoding같다.
		 *	때문에 한글이 꺠진다..ㅠ
		 *
		 */
		//config.setCompleteAutoload(true);

		// 이게 뭐하는건지 꼭 알아보자.
		config.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
		return config;
	}
	
	
	/******************** JADE ********************/
	/* @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setBasePath("/WEB-INF/views/");
        templateLoader.setEncoding("UTF-8");
        templateLoader.setSuffix(".jade");
        return templateLoader;
    }

    @Bean
    public JadeConfiguration jadeConfiguration() {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setCaching(false);
        configuration.setTemplateLoader(templateLoader());
        return configuration;
    }

    @Bean
    public ViewResolver jadeViewResolver() {
        JadeViewResolver viewResolver = new JadeViewResolver();
        viewResolver.setConfiguration(jadeConfiguration());
        return viewResolver;
    }*/
	
}

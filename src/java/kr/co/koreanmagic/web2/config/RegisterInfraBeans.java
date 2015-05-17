package kr.co.koreanmagic.web2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("resource.properties")
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
		PropertySourcesPlaceholderConfigurer pp = new PropertySourcesPlaceholderConfigurer();
		return pp;
	}
	
	

}

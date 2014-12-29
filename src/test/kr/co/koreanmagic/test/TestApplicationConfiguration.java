package kr.co.koreanmagic.test;

import org.apache.log4j.Logger;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan({
	"kr.co.koreanmagic.test.classes"
	})
@EnableAspectJAutoProxy
public class TestApplicationConfiguration {
	Logger logger = Logger.getLogger("test");

	public TestApplicationConfiguration() {
		logger.debug("TestApplicationConfiguration()");
	}
	
	/*@Bean
	public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator(){
		AnnotationAwareAspectJAutoProxyCreator proxy =  new AnnotationAwareAspectJAutoProxyCreator();
		logger.debug(proxy);
		return proxy;
	}*/

	
}

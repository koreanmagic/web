package kr.co.koreanmagic.test;
import kr.co.koreanmagic.test.classes.aspect.AspectJTEST;
import kr.co.koreanmagic.test.classes.pojo.AspectClass;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestApplicationConfiguration.class})
public class 스프링컨테이너테스트 extends ConfigurableDispatcherServlet {

	
	Logger logger = Logger.getLogger("test");
	@Autowired AspectClass c;
	@Autowired AspectJTEST at;
	
	
	@Test
	public void test1() throws Exception {
		logger.debug(c.say("아뿔로"));
	}
	
	

}

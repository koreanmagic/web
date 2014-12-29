package kr.co.koreanmagic.test.logg;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class Twist {

	Path path = Paths.get("G:/app/Projects/web2/webapp/WEB-INF/view/ftl");
	Logger logger = Logger.getLogger(getClass());
	
	@Test
	public void test() throws Exception {
		log(getClass().getName());
	}
	
	private void log(Object obj) {
		System.out.println(obj);
	}
	

}

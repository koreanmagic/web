package kr.co.koreanmagic.web.support.nav;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.web.support.page.PageContext;
import kr.co.koreanmagic.web.support.page.PageContextImpl;

import org.junit.Test;

public class TestNavigator {

	//@Test
	public void test() {
		
		
		PageContextImpl context1 = new PageContextImpl("/seoul");
		PageContext context1_1 = context1.resolve("gangnam");
		PageContext context1_2 = context1.resolve("bangbae");
		PageContext context1_3 = context1.resolve("yangjae");
		
		PageContextImpl context2 = new PageContextImpl("/suwon");
		PageContext context2_1 = context2.resolve("top");
		PageContext context2_2 = context2.resolve("hwaseo");
		PageContext context2_3 = context2.resolve("yooljeon");
		
		GeneralNavigator nav = new GeneralNavigator(new PageContextImpl(""));
		
		GeneralNavigator nav1 = new GeneralNavigator(context1);
		nav1.addMenu(context1_1);
		nav1.addMenu(context1_2);
		nav1.addMenu(context1_3);
		
		GeneralNavigator nav2 = new GeneralNavigator(context2);
		nav2.addMenu(context2_1);
		nav2.addMenu(context2_2);
		nav2.addMenu(context2_3);
		
		nav.addMenu(nav1);
		nav.addMenu(nav2);
		
		nav.setCurrent(context2_1.getPath());
		//log(print(nav));
		
		nav.setCurrent(context1_1.getPath());
		//log(print(nav));
		
		log(nav.find(context2_3));
		
		
	}
	
	
	@Test
	public void 업로드() throws Exception {
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("nav.properties"));
		
		GeneralNavigator rootNav = new GeneralNavigator("/");
		
		log(print(NavigatorLoader.loader(prop, rootNav)));
	}
	
	
	
	private String print(PageContext context) {
		String result = "path: ";
		result += context.getPath();
		result += " // name: " + context.toString();
		return result;
	}
	
	private String print(Navigator nav) {
		String result = "";
		if(nav.pageContext() != null) result += nav + "(" + nav.isCurrent() + ") \n";
		
		for(Navigator n : nav.childs()) {
			result += print(n);
		}
		return result;
	}
	
	static class Nav extends GeneralNavigator {

		protected Nav(String path) {
			super(new PageContextImpl(path));
		}
		
	}
	
	
	private void log(Object obj) {
		System.out.println(obj);
	}

}

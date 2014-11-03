package kr.co.koreanmagic.web.support.nav;

import java.util.Properties;

import kr.co.koreanmagic.web.support.page.MenuContextImpl;

import org.junit.Test;

public class TestNavigator2 {

	@Test
	public void test() {
		
		MenuContextImpl p = new MenuContextImpl("0"); 
		MenuContextImpl p1 = new MenuContextImpl("1"); 
		MenuContextImpl p2 = new MenuContextImpl("2"); 
		MenuContextImpl p3 = new MenuContextImpl("3"); 
		MenuContextImpl p4 = new MenuContextImpl("4");
		
		GeneralMenuNavigator nav = new GeneralMenuNavigator(p);
		GeneralMenuNavigator child = nav.addMenu(p1).addMenu(p2).addMenu(p3).addMenu(p4);
		
		log(child.getPath());
		
		
		GeneralMenuNavigator find = nav.protoFind(child.getParent().getParent().getPath());
		log(find.getPath());
		
		
	}
	
	
	//@Test
	public void 업로드() throws Exception {
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("nav.properties"));
		
		GeneralNavigator rootNav = new GeneralNavigator("/");
		
	}
	
	
	
	
	private void log(Object obj) {
		System.out.println(obj);
	}

}

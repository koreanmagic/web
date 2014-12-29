package kr.co.koreanmagic.web2.support.nav;

import kr.co.koreanmagic.web.support.nav.Navigator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class NavigatorFactory implements FactoryBean<Navigator> {

	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Navigator getObject() throws Exception {
		return getMenu();
	}

	@Override
	public Class<Navigator> getObjectType() {
		return Navigator.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	private AdminNavigator getMenu() {
		return new AdminNavigator("admin").setName("관리자")
				
			.addMenu(
				new AdminNavigator("work").setName("작업메뉴")
				.addMenu(new AdminNavigator("wait").setName("대기"))
				.addMenu(new AdminNavigator("working").setName("작업"))
				.addMenu(new AdminNavigator("printing").setName("인쇄"))
				.addMenu(new AdminNavigator("giving").setName("납품"))
				.addMenu(new AdminNavigator("complete").setName("완료"))
			).addMenu(
				new AdminNavigator("customer").setName("거래처")
				.addMenu(new AdminNavigator("VIP").setName("VIP"))
				.addMenu(new AdminNavigator("new").setName("신규"))
			);
		
	}


}

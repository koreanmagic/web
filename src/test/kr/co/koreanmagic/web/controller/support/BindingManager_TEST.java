package kr.co.koreanmagic.web.controller.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import kr.co.koreanmagic.test.TestApplicationConfiguration;
import kr.co.koreanmagic.web.controller.support.webdata.BidingManager;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestApplicationConfiguration.class})
public class BindingManager_TEST {

	
	Logger logger = Logger.getLogger("test");
	Map<String, Object> map;
	
	@Autowired(required=false) PropertyEditorRegistrySupport m;
	
	@Before
	public void init() {
		this.map = new HashMap<>();
		logger.debug(m);
	}
	
	@Test
	public void test() throws Exception {
		put("name", "고정철");
		put("age", "10");
		
		BidingManager<TestBean> bm = new BidingManager<>(TestBean.class);
		TestBean bean = bm.populate(new TestBean(), map);
		
		assertThat(bean.toString(), is("고정철/10/"));
	}
	
	
	
	private void put(String key, Object value) {
		map.put(key, value);
	}
	
	private void put(String[] keys, Object[] values) {
		int len = keys.length;
		if(len != values.length) throw new RuntimeException("서로 갯수가 다릅니다.");
		
		for(int i = 0; i < len; i++) {
			map.put(keys[i], values[i]);
		}
	}
	
	
	public static class TestBean {
		String name;
		int age;
		String level;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		
		@Override
		public String toString() {
			return converter(name) + "/" + converter(age) + "/" + converter(level);
		}
		private String converter(Object obj) {
			if(obj == null) return "";
			String result = obj.toString();
			if(result.equals("0")) return "";
			return result;
		}
		
		
	}
	

}



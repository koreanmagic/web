package kr.co.koreanmagic.web.velocity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityTEST {
	
	private static String PATH = "/webapp/WEB-INF/";
	
	
	public static void main(String[] args) throws Exception {
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		
		VelocityContext context = new VelocityContext(); // 벨로시티 정보를 담은 객체
		Template template;
		template = ve.getTemplate(PATH + "view/velocity/example.vm"); // 벨로시티 템플릿 생성 (벨로시티 파일을 파싱함)
		
		
		context.put("petList", list());
		context.put("name", "고정철");
		context.put("domain", new Domain("고정철", 34));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		if(template != null)
			template.merge(context, writer); // 템플릿과 정보객체를 섞는다.
		
		writer.flush();
		writer.close();
		
	}
	
	static List<Map<String, String>> list() {
		ArrayList<Map<String, String>> list = new ArrayList();
		   Map<String, String> map = new HashMap<>();

		   map.put("name", "horse");
		   map.put("price", "$100.00");
		   list.add( map );

		   map = new HashMap<>();
		   map.put("name", "dog");
		   map.put("price", "$59.99");
		   list.add( map );

		   map = new HashMap<>();
		   map.put("name", "bear");
		   map.put("price", "$3.99");
		   list.add( map );

		   return list;
	}
	
	public static class Domain {
		private String name;
		private int old;
		public Domain() {}
		public Domain(String name, int old) { this.name = name; this.old = old; }
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getOld() {
			return old;
		}
		public void setOld(int old) {
			this.old = old;
		}
		@Override public String toString() { return "임마"; }
	}
	
	private static void out(Object obj) {
		System.out.println(obj);
	}

}

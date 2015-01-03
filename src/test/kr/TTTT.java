package kr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TTTT {

	@Test
	public void test() throws Exception {
		Path path = Paths.get("g:/data.txt");
		
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> lines = Files.readAllLines(path, Charset.forName("euc-kr"));
		
		String[] s = null, labels = {"date", "customer", "item", "memo", "size", "count", "cost", "pay", "constructor"};
		
		int i=0, len = labels.length;
		Map<String, Object> model = null;
		for(String l : lines) {
			s = l.split(",");
			model = new HashMap<>();
			for(i=0;i<len;i++) {
				model.put(labels[i], s[i]);
			}
			result.add(model);
		}
			
	}
	
	
	private<T> T log(T t) {
		System.out.println(t);
		return t;
	}

}

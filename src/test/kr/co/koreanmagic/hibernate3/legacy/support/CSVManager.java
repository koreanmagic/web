package kr.co.koreanmagic.hibernate3.legacy.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * CSV파일을 읽어들여 List<String>으로 만들어준다.
 * ※ 이떄 첫 줄은 무시한다.
 */
public class CSVManager {
	
	public static List<String[]> pull(Path path, String charset) {
		return pull(path, charset, ",");
	}
	public static List<String[]> pull(Path path, String charset, String separater) {
		try {
			
			List<String> lines = Files.readAllLines(path, Charset.forName(charset));
			List<String[]> result = new ArrayList<>();
			
			// 파일에서 첫줄은 건너띈다.
			for(int i = 1, length = lines.size(); i < length; i++) {
				String[] values = lines.get(i).split(separater);
				result.add(values);
			}
			
			return result;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/*
	 * CSV파일을 읽어들여 Map<String, Object>로 변환해준다!
	 * 
	 * ① CSV파일을 읽어들여 '#' 문자가 포함되어 있는 line 그 다음줄부터 값을 읽어들인다. 
	 * 
	 * CSV 각 Value에 해당하는 key값을 설정해야 한다.
	 * '#' 문자 뒤에 쉼표로 구분해서 나열하면 된다.
	 */
	public static final String KEYS = "#";
	
	public static List<Map<String, Object>> readCSV(Path path, String incoding) throws IOException {
		return readCSV(Files.newInputStream(path), incoding);
	}
	public static List<Map<String, Object>> readCSV(InputStream is, String incoding) throws IOException {
		
		int len = 0;
		String[] values = null, keys = null;
		String line = null;
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(is, incoding));) {
			
			/*
			 * '#'문자가 발견될때까지 루프를 돌린다.
			 * '#'문자가 나타난 바로 그 줄부터 파싱이 시작된다.
			 */
			while((line = br.readLine()) != null)
				if(line.contains(KEYS)) break;
			
			/** ▒▒▒▒▒▒▒▒▒▒  Key 목록 파싱  ▒▒▒▒▒▒▒▒▒▒ **/
			String key = line.substring(
									line.indexOf(KEYS) + KEYS.length(),
									line.length()
								);
			keys = key.split(",");
			len = keys.length;
			
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> map = null;
			
			
			/** ▒▒▒▒▒▒▒▒▒▒  실제 목록 파싱이 시작되는 루프  ▒▒▒▒▒▒▒▒▒▒ **/
			while((line = br.readLine()) != null) {
				int i = 0;
				if(!line.contains(","))
					throw new RuntimeException(line + " <-- CSV 형식이 아닙니다.");
				
				map = new HashMap<>();
				values = line.split(",");
				
				if(len != values.length)
					throw new RuntimeException(line + " <-- 키 갯수와 값 갯수가 일치하지 않습니다. \n "
													+ "키 갯수: " + len + "개 / 값 갯수: " + values.length + "개");
				
				for(;i < len; i++) {
					map.put(keys[i], nullCheck(values[i]));
				}
				list.add(map);
			}
			
			return list;
		}
		
	}
	
	private static String nullCheck(Object s) {
		if(s == null ||s.toString().length() == 0)
			return null;
		return s.toString().trim();
	}
	
}

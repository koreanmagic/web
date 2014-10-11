package kr.co.koreanmagic.hibernate.legacy;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.hibernate.Session;

/*
 * CSV파일을 읽어들여 List<String>으로 만들어준다.
 * ※ 이떄 첫 줄은 무시한다.
 */
public class CSVPull {
	
	public static List<String[]> pull(Path path, String charset) {
		return pull(path, charset, ",");
	}
	public static List<String[]> pull(Path path, String charset, String separater) {
		try {
			
			List<String> lines = Files.readAllLines(path, Charset.forName(charset));
			List<String[]> result = new ArrayList<>();
			
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
	 * 배열이 비어있는지, 빈 문자인지 확인
	 * 있으면 문자열 반환,
	 * 없으면 null 반환
	 */
	static String nullCheck(String[] values, int i) {
		String value = null;
		if(values.length > i && (value = values[i]) != null && value.length() != 0) {
			return value;
		}
		return null;
	}
	
	
}

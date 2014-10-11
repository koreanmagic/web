package kr.co.koreanmagic.web.file;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkFile;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkFactory {
	
	private final static Logger logger = Logger.getLogger(WorkFactory.class);
	
	
	// 파일명을 넣어주면 모두 파싱해서 Work 객체로 만들어준다.
	public static Work fillWork(String fileName) {
		
		Work work = new Work();
		work.setInsertTime(selectDate(fileName)); // 날짜
		work.setCustomer(selectCustomer(fileName)); // 거래처명
		work.setItem(selectItem(fileName)); // 아이템
		work.setCount(selectCount(fileName)); // 건수
		work.setNumber(selectNumber(fileName)); // 수량
		work.setBleed(selectBleed(fileName)); // 재단선
		work.setSize(selectSize(fileName)); // 사이즈
		work.setTag(selectTag(fileName)); // 메모
		work.setUnit(selectUnit(fileName)); // 단위
		work.setFiletype(selectFileType(fileName)); // 확장자
		
		return work;
	}
	
	
	
	private static final Pattern p_id = Pattern.compile("\\b(\\d{9})"); // 아이디
	public static String selectId(String string) {
		return getResult(p_id, string);
	}
	
	
	private static final Pattern p_size = Pattern.compile("(\\d{2,3}-\\d{2,3})"); // 사이즈
	public static String selectSize(String string) {
		return getResult(p_size, string);
	}

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final Pattern p_date = Pattern.compile("(\\b\\d{6})"); // 사이즈
	public static Timestamp selectDate(String index) {
		index = getResult(p_date, index);
		String year = "20" + index.substring(0, 2);
		String month = index.substring(2, 4);
		String day = index.substring(4, 6);
		
		
		DateTime dt = DATE_FORMAT.parseDateTime(year + "-" + month + "-" + day);
		return new Timestamp(dt.getMillis());
	}
	
	private static final Pattern p_customer = Pattern.compile("\\d{6}_([가-힣a-zA-Z0-9&]+)"); // 거래처명
	public static String selectCustomer(String string) {
		String customer = getResult(p_customer, string);
		return invalidName(customer, items) ? null : customer;
	}
	
	
	
	private static final Pattern p_unit = Pattern.compile("\\d{2,3}-\\d{2,3}([a-zA-Z]{2})"); // 단위
	public static String selectUnit(String string) {
		return getResult(p_unit, string);
	}
	
	
	/*
	 * 재단선은 오직 (2).파일명 의 형태일때만 뽑아낼 수 있다. 기본값은 0이다.
	 */
	private static final Pattern p_bleed = Pattern.compile("\\((\\d)\\)\\."); // 재단선
	public static int selectBleed(String string) {
		String result = getResult(p_bleed, string);
		if(result == null) return 0;
		return Integer.valueOf(result);
	}

	
	/*
	 * 수량의 기본값은 0이다. [1-1000] 형태가 아니면 수량은 추출해낼 수가 없다.
	 */
	private static final Pattern p_number = Pattern.compile("\\[\\d+-(\\d+)\\]"); // 수량
	public static int selectNumber(String string) {
		String result = getResult(p_number, string);
		if(result == null) return 0;
		return Integer.valueOf(result);
	}
	
	/*
	 * (10건) 혹은 [1-1000] 등에서 건수를 추출해낸다.
	 */
	private static final Pattern[] p_counts = { Pattern.compile("\\[(\\d+)-\\d+\\]"),
												Pattern.compile("(\\d+)건"),
												 Pattern.compile("_\\[(\\d+)\\]_"),
												};
	public static int selectCount(String string) {
		String result = null;
		for(Pattern p : p_counts) {
			result = getResult(p, string);
			if(result != null) break;
		}
		return result == null ? 1 : Integer.valueOf(result);
	}
	
	
	private static final Pattern p_fileType = Pattern.compile("\\.([a-zA-Z]{2,3}$)"); // 확장자
	public static String selectFileType(String fileName) {
		return getResult(p_fileType, fileName);
	}
	
	
	/*
	 * 같은 거래를 구분하게 해주는 메모
	 */
	private static final Pattern p_tag = Pattern.compile("\\b\\d{6}_[가-힣a-zA-Z0-9]+\\((.+?)\\)"); // 확장자
//	private static final Pattern p_tag = Pattern.compile("\\b\\d{9}_[가-힣a-zA-Z0-9]+_?\\(?(.+?)\\)?_"); // 확장자
	public static String selectTag(String fileName) {
		String result =  getResult(p_tag, fileName);
		return selectTag0(result);
	}
	private static String selectTag0(String result) {
		result = invalidName(result, items) ? null : result; // 품목이 적혀있는지 확인
		if(result != null) {
			result = matchRegex(result, "(건|^\\d+$)") ? null : result; // 불필요한 단어가 섞여있는지?
		}
		return result;
	}
	
	
	/*
	 * 가장 중요한 메서드인 아이템 추출 메서드
	 * 정말 다양한 방법을 통해 아이템명을 추출해낸다.
	 */
	private static final String[] items  = {"박스", "다대봉투", "명함", "전단", "포스터", "책", "티켓", "청첩장", "소봉투", "자켓봉투",
		"리플렛", "카다로그", "팜플렛", "자격증", "신분증", "빌지", "영수증", "NCR", "보고서", "엽서", "스티커", "주문서", "양식지",
		"마스터", "카렌다", "쿠폰", "초대권", "명찰", "브로슈어", "메달", "연하장", "노트", "요지", "대봉투", "이쑤시개", "초대장", "봉투"
		};
	public static String selectItem(String fileName) {
		String item = null;
		
		item = decideItem(fileName, items);
		
		if(item == null) {
			item = decideItemBySize(selectSize(fileName));
		}
		if(item == null) {
			item = decideItemByCustomer(selectCustomer(fileName));
		}
		
		return item;
	}
	
	
	// 해당목록이 파일명에 적혀있는지 확인해본다.
	private static String decideItem(String fileName, String[] items) {
		for(int i = 0; i < items.length; i++) {
			if(fileName.contains(items[i])) {
				return items[i];
			}
		}
		return null;
	}

	// 한가지 품목만 하는 업체들 솎아냄
	private static String decideItemByCustomer(String customer) {
		switch(customer) {
		
		case "무한유통" :
			return "스티커";
		case "신명" :
			return "스티커";
		
			default :
				return null;
		}
	}
	
	
	private static String decideItemBySize(String size) {
		if(size == null) return null;
		return ItemParser.searchItem(size);
	}

	
	// 불필요한 단어가 걸렸는지 검증해주는 메서드
	private static boolean invalidName(String target, String[] list) {
		if(target == null) return false;
		for(String l : list) {
			if(target.equals(l))
				return true;
		}
		return false;
	}
	// 불필요한 단어가 걸렸는지?
	private static boolean invalidContains(String target, String[] list) {
		if(target == null) return false;
		for(String l : list)
			if(target.contains(l)) return true;
		return false;
	}
	
	private static boolean matchRegex(String target, String regex) {
		if(target==null) return false;
		return Pattern.compile(regex).matcher(target).find();
	}


	

	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 유틸 메서드 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */ 
	private static String getResult(Pattern pattern, String text) {
		Matcher m = getMatcher(pattern, text);
		if(m.find() && m.groupCount() == 1)
			return m.group(1);
		return null;
	}
	
	
	private static Matcher getMatcher(Pattern pattern, String text) {
		Matcher m = pattern.matcher(text);
		return m;
	}
	
	


	
	
	
	

	
}

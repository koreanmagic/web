package kr.co.koreanmagic.web.controller.convertor;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkDateConverter implements Converter<String, Timestamp> {

	private static String pattern = "^(19[7-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
	private static DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Timestamp convert(String source) {
		Timestamp timestamp = null;
		if(source.equals("today") || source.length() == 0)  // today가 들어오면 오늘 날짜 내보낸다.
			timestamp = createStamp();
		else if(source.equals("tomarrow")) // 다음날짜
			timestamp = createStamp(3); // 
		else if(source.matches(pattern)) // 2014-01-01
			timestamp =  createStamp(new DateTime(source));
		else
			timestamp = createStamp(f.parseDateTime(source));
		
		return timestamp;
	}
	
	private Timestamp createStamp() {
		return new Timestamp(new DateTime().getMillis());
	}
	
	private Timestamp createStamp(int days) {
		DateTime dt= new DateTime();
		dt = dt.plusDays(days);
		return createStamp(dt);
	}
	
	private Timestamp createStamp(DateTime dt) {
		return new Timestamp(dt.getMillis());
	}
}

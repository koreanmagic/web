package kr.co.koreanmagic.web.controller.formatter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

@Component
public class NumberToKoreanFormatImpl implements AnnotationFormatterFactory<NumberToKoreanFormat> {

	// 이 포매터가 지원하는 객체 종류 (여기에 없으면 @Formatter가 붙어있어도 검증하지 않는다.) 
	@Override
	public Set<Class<?>> getFieldTypes() {
		return new HashSet<Class<?>>(
				Arrays.asList(
						new Class<?>[]{
							Integer.class, int.class	
						})
				);
	}
	
	@Override
	public Printer<Integer> getPrinter(NumberToKoreanFormat annotation, Class<?> fieldType) {
		return new IntegerImpl();
	}

	@Override
	public Parser<Integer> getParser(NumberToKoreanFormat annotation, Class<?> fieldType) {
		return new IntegerImpl();
	}

	static class IntegerImpl implements Formatter<Integer> {
		
		@Override
		public String print(Integer fieldValue, Locale locale) {
			
			switch(fieldValue) {
				case 1 : return "일";
				case 2 : return "이";
				case 3 : return "삼";
				case 4 : return "사";
				case 5 : return "오";
				case 6 : return "육";
				case 7 : return "칠";
				case 8 : return "팔";
				case 9 : return "구";
				case 0 : return "영";
				default : return "X";
			}
		}

		@Override
		public Integer parse(String fommated, Locale locale) throws ParseException {
			return Integer.valueOf(fommated);
		}
	}

}

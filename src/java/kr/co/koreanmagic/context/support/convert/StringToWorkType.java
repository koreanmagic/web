package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToWorkType implements Converter<String, WorkType> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public WorkType convert(String source) {
		return WorkType.get(Integer.parseInt(source));
	}
}

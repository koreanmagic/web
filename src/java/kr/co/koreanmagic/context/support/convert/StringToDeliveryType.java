package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.DeliveryType;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToDeliveryType implements Converter<String, DeliveryType> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	@Transactional
	public DeliveryType convert(String source) {
		DeliveryType result = DeliveryType.get(Integer.parseInt(source));
		return result;
	}
}

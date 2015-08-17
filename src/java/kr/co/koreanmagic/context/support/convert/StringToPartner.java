package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToPartner implements Converter<String, Partner> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired SessionFactory factory;
	
	@Override
	@Transactional(readOnly=true)
	public Partner convert(String source) {
		return (Partner)factory.getCurrentSession().load(Partner.class, Long.parseLong(source));
	}
}

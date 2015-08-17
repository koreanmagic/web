package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.service.SubcontractorService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToSubcontractor implements Converter<String, Subcontractor> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired SubcontractorService service;
	
	@Override
	@Transactional(readOnly=true)
	public Subcontractor convert(String source) {
		return service.load(Long.valueOf(source));
	}
}

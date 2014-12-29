package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.service.SubcontractorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NameToSubcontractorConverter implements Converter<String, Subcontractor> {

	@Autowired SubcontractorService service;
	
	@Override
	public Subcontractor convert(String source) {
		return service.findByName(source);
	}

}

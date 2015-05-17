package kr.co.koreanmagic.context.support.convert;

import kr.co.koreanmagic.hibernate3.mapper.domain.WorkResourceFile;
import kr.co.koreanmagic.service.WorkResourceFileService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StringToWorkResourceFile implements Converter<String, WorkResourceFile> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired WorkResourceFileService service;
	
	@Override
	@Transactional(readOnly=true)
	public WorkResourceFile convert(String source) {
		return service.get(Long.valueOf(source));
	}
}

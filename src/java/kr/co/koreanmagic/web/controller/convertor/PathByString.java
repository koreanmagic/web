package kr.co.koreanmagic.web.controller.convertor;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PathByString implements Converter<String, Path> {

	Logger log = Logger.getLogger(getClass());
	
	@Override
	public Path convert(String source) {
		return Paths.get(source);
	}

}

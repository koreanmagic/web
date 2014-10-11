package kr.co.koreanmagic.web.config;

import kr.co.koreanmagic.web.db.mybatis.config.ConfigureMyBatis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebMvcConfig.class, ConfigureMyBatis.class})
public class WebMvcStartUp {

}

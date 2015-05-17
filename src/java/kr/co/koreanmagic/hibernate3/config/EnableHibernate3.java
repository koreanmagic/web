package kr.co.koreanmagic.hibernate3.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value={HibernateConfigAware.class, DatabaseConfiguration.class})
public @interface EnableHibernate3 {
}

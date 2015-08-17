package kr.co.koreanmagic.hibernate.customtype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.koreanmagic.commons.Utils;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

public class BusinessNumberUserType implements UserType {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public int[] sqlTypes() {
		//logger.debug("");
		return new int[]{StringType.INSTANCE.sqlType()};
	}

	/*
	 * 돌려주는 값 타입
	 */
	@Override
	public Class<?> returnedClass() {
		return ThreeNumber.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		logger.debug("equals --> source : " + nullSafeString(x) + " / other : " + nullSafeString(y) );
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		StringBuilder result = new StringBuilder("[values] ");
		if(names.length != 0) logger.debug("[names] " + String.join(",", names));
		Object value = null;
		for(String name : names) {
			value = rs.getObject(name);
			result.append(name).append(" : ").append(value).append("  ");
		}
		logger.debug(result);
		return value;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		print("DB 입력값 : " + nullSafeString(value) + " / 컬럼 인덱스 : " + index);
		//st.setLong(index, new Long(Long.parseLong(value.toString())));
		st.setString(index, value.toString());
		
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		//logger.debug(nullSafeString(value));
		return null;
		//return value;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		print(nullSafeString(value));
		return null;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		print(nullSafeString(owner));
		return null;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		print(String.format("original : %s / target : %s / owner : %s", original, target, nullSafeString(owner)));
		return original;
	}

	private String nullSafeString(Object obj) {
		if(obj == null) return "null";
		return obj.toString();
	}
	
	private void print(Object obj) {
		System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ " + obj);
	}
}

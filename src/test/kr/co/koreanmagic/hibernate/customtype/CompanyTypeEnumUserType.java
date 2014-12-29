package kr.co.koreanmagic.hibernate.customtype;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import kr.co.koreanmagic.commons.KoUtils;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype.CompanyType;

import org.hibernate.HibernateException;
import org.hibernate.type.StringType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

public class CompanyTypeEnumUserType implements EnhancedUserType, ParameterizedType {

	@Override
	public int[] sqlTypes() {
		return new int[] {StringType.INSTANCE.sqlType()};
	}

	@Override
	public Class<?> returnedClass() {
		return CompanyType.class;
	}
	

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return true;
		//return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		return CompanyType.values()[
		                            	rs.getInt(names[0])
		                            ];
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		st.setInt(index, ((CompanyType)value).ordinal());
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return null;
	}

	@Override
	public void setParameterValues(Properties parameters) {
	}

	@Override
	public String objectToSQLString(Object value) {
		return null;
	}

	@Override
	public String toXMLString(Object value) {
		return null;
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		return null;
	}

}

package kr.co.koreanmagic.hibernate3.DB이전.support;


public interface NameConvertor {
	
	String convert(String name) throws NoSuchMasterNameException;
	int size();

}

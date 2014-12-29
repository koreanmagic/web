package kr.co.koreanmagic.hibernate3.legacy.support;


public interface NameConvertor {
	
	String convert(String name) throws NoSuchMasterNameException;
	int size();

}

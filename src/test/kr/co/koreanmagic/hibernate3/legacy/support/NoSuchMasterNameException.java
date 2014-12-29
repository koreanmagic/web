package kr.co.koreanmagic.hibernate3.legacy.support;

public class NoSuchMasterNameException extends RuntimeException {

	String name;
	
	public NoSuchMasterNameException(Exception e) {
		super(e);
	}
	public NoSuchMasterNameException(String e) {
		super(e);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}

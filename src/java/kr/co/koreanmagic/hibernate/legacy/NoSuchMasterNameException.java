package kr.co.koreanmagic.hibernate.legacy;

public class NoSuchMasterNameException extends RuntimeException {

	public NoSuchMasterNameException(Exception e) {
		super(e);
	}
	public NoSuchMasterNameException(String e) {
		super(e);
	}
	
}

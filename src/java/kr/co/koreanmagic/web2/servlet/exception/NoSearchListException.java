package kr.co.koreanmagic.web2.servlet.exception;

public class NoSearchListException extends RuntimeException {
	
	private String keyword;
	
	static final long serialVersionUID = -3387516993124229948L;
	
	public NoSearchListException(String msg) {
		super(msg);
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return this.keyword;
	}
	
}

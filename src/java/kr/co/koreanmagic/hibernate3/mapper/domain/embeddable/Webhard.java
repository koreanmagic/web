package kr.co.koreanmagic.hibernate3.mapper.domain.embeddable;

import javax.persistence.Embeddable;

@Embeddable
public class Webhard {
	
	private String userId;
	private String password;
	private String url;
	
	public String getUserId() {
		return userId == null ? "" : userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password == null ? "" : password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUrl() {
		return url == null ? "" : url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		if(getUserId().length() == 0 && getPassword().length() == 0) return "";
		return getUserId() + " / " + getPassword();
	}

}

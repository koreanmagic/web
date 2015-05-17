package kr.co.koreanmagic.hibernate3.mapper.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Website {
	
	private String url;
	private String id;
	private String password;
	
	
	@Column(name="website_url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="website_id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="website_password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		if(getUrl() == null) return "";
		return getUrl();
	}

}

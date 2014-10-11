package kr.co.koreanmagic.hibernate3.mapper.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
	
	private String id;
	private String host;
	
	public Email() {}
	public Email(String id, String host) {
		setId(id);
		setHost(host);
	}
	public Email(String...s) {
		setId(s[0]);
		setHost(s[1]);
	}
	
	@Column(name="email_id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="email_host")
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString() {
		return getId() + "@" + getHost();
	}

}

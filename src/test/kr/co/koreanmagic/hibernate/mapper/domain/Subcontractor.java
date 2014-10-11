package kr.co.koreanmagic.hibernate.mapper.domain;

import static kr.co.koreanmagic.commons.KoStringUtils.nullValue;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.koreanmagic.hibernate.mapper.embeddable.Bank;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Website;

@Entity
@Table(name="subcontractor")
public class Subcontractor extends Partner {
	
	/* 홈페이지 */
	private Website website;
	
	/* 웹하드 */
	private Website webhard;
	
	private Bank bank;
	
	
	public Subcontractor() { }

	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}

	@AttributeOverrides({
		@AttributeOverride(name="url", column=@Column(name="webhard_url")),
		@AttributeOverride(name="id", column=@Column(name="webhard_id")),
		@AttributeOverride(name="password", column=@Column(name="webhard_password")),
	})
	public Website getWebhard() {
		return webhard;
	}
	public void setWebhard(Website webhard) {
		this.webhard = webhard;
	}
	
	
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	@Override
	public String toString() {
		return String.format("[%s / %s / %s / %s]", getName(), nullValue(getMobile(), ""), nullValue(getTel(), ""), nullValue(getFax(), ""));
	}
	
}

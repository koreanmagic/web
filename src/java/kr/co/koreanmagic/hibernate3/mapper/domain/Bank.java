package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;
import kr.co.koreanmagic.hibernate3.mapper.domain.marker.PartnerMember;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
getterVisibility = Visibility.NONE, 
setterVisibility = Visibility.NONE)

@Entity
@Table(name="banks")
public class Bank implements PartnerMember {
	
	@JsonProperty private Long id;
	@JsonProperty private Long version;
	@JsonProperty private BankName bankName;
	@JsonProperty private String accountNum;
	@JsonProperty private String holder;
	
	@JsonProperty private Timestamp updateTime;
	@JsonProperty private Partner partner;
	
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Version
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	
	// 광범위하게 사용할 목적이다.
	/*
	 * fetch의 기본값이 EAGER이므로
	 * LAZY를 지정해줘야 LazyToOne의 PROXY가 동작한다.
	 */
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@LazyToOne(LazyToOneOption.PROXY)
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="bank_name")
	@ForeignKey(name="fk_bank")
	public BankName getBankName() {
		return bankName;
	}
	public void setBankName(BankName bankNmae) {
		this.bankName = bankNmae;
	}

	@Column(name="bank_num")
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	
	@Column(name="update_time", insertable=false, updatable=false,
			columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNum == null) ? 0 : accountNum.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Bank other = (Bank) obj;
		return other.getId().equals(getId());
	}
	
	@Override
	public String toString() {
		return String.format("[%s] %s (%s)", getBankName(), getAccountNum(), getHolder());
	}

	
}

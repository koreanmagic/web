package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.marker.PartnerMember;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
getterVisibility = Visibility.NONE, 
setterVisibility = Visibility.NONE)


@Entity
@Table(name="addresses")
public class Address implements PartnerMember {
	
	@JsonProperty private Long id;
	@JsonProperty private Long version;
	
	@JsonProperty private String text;
	
	@JsonProperty private String name;
	@JsonProperty private ThreeNumber mobile;
	
	@JsonProperty private Timestamp updateTime;
	@JsonProperty private String memo;
	private Partner partner;
	
	
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
	
	@Column(nullable=false, name="address")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Columns(columns={
			@Column(name="mobile_num1", nullable=true),
			@Column(name="mobile_num2", nullable=true),
			@Column(name="mobile_num3", nullable=true),
	})
	@Type(type="kr.co.koreanmagic.hibernate3.mapper.customtype.BusinessNumberCompositeUserType")
	public ThreeNumber getMobile() {
		return mobile;
	}
	public void setMobile(ThreeNumber mobile) {
		this.mobile = mobile;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Address other = (Address) obj;
		return other.getId().equals(getId());
	}
	@Override
	public String toString() {
		return String.format("주소: %s / 이름: %s / 전화번호: %s", getText(), getName(), getMobile().toString());
	}

}

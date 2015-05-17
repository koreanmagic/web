package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.marker.PartnerMember;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
getterVisibility = Visibility.NONE, 
setterVisibility = Visibility.NONE)

@Entity
@Table(name="managers")
@BatchSize(size=5)
public class Manager implements PartnerMember {
	
	@JsonProperty public Long id;
	@JsonProperty private Long version;
	
	@JsonProperty private String name;				// 이름
	@JsonProperty private String position;			// 직책
	@JsonProperty private ThreeNumber mobile;			// 휴대전화
	@JsonProperty private ThreeNumber tel;			// 유선전화
	@JsonProperty private ThreeNumber fax;			// 팩스
	@JsonProperty private Email email;				// 이메일
	
	@JsonProperty private String memo;				// 메모
	@JsonProperty private Date insertTime;
	
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
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@LazyToOne(LazyToOneOption.PROXY)
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	
	
	@Columns(columns={
			@Column(name="tel_num1", nullable=true),
			@Column(name="tel_num2", nullable=true),
			@Column(name="tel_num3", nullable=true),
	})
	@Type(type="kr.co.koreanmagic.hibernate3.mapper.customtype.BusinessNumberCompositeUserType")
	public ThreeNumber getTel() {
		return tel;
	}
	public void setTel(ThreeNumber tel) {
		this.tel = tel;
	}

	
	@Columns(columns={
			@Column(name="fax_num1", nullable=true),
			@Column(name="fax_num2", nullable=true),
			@Column(name="fax_num3", nullable=true),
	})
	@Type(type="kr.co.koreanmagic.hibernate3.mapper.customtype.BusinessNumberCompositeUserType")
	public ThreeNumber getFax() {
		return fax;
	}
	public void setFax(ThreeNumber fax) {
		this.fax = fax;
	}

	@Column(name="email")
	public Email getEmail() {
		return email == null ? email = new Email() : email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	
	@Column(name="insert_time", nullable=false, updatable=false) 
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name="memo")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/*
	 * equals 비교시 getClass()로 형 정보를 검사하는건 지운다.
	 * proxy가 들어올경우 형 정보가 일치하지 않는다.
	 * 그리고 메서드를 통한 값입출력으로 설정했을 경우, 필드 자체로 접근하면 값을 읽어오지 못한다.
	 * 따라서 메서드접근 모드일때는 메서드로 가지고 온다.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Manager other = (Manager) obj;
		return getId().equals(other.getId());
	}
	@Override
	public String toString() {
		return getName();
	}
	
}

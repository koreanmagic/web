package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Webhard;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Website;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;


@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
				getterVisibility = Visibility.NONE, 
				setterVisibility = Visibility.NONE)

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="partners")
public abstract class Partner {
	
	
	@JsonProperty private Long id;
	@JsonProperty private Long version;
	
	@JsonProperty private ThreeNumber bizNum;				// 사업자번호
	@JsonProperty private String bizCondition;			// 업태
	@JsonProperty private String bizTypes;				// 종목
	
	@JsonProperty private String name;					// 회사 이름
	@JsonProperty private String otherName;				// 다른 이름
	
	@JsonProperty private String ceoName;					// 사업주 이름
	@JsonProperty private ThreeNumber mobile;				// 사업주 핸드폰
	@JsonProperty private ThreeNumber tel;				// 대표전화
	@JsonProperty private ThreeNumber fax;				// 대표팩스
	@JsonProperty private Email email;					// 대표 이메일
	
	private List<Manager> manager;			// 담당자들
	
	@JsonProperty private Website web;					// 홈페이지
	@JsonProperty private Webhard webhard;				// 웹하드
	
	private List<Address> address;			// 주소
	private List<Bank> banks;				// 계좌
	
	@JsonProperty private String memo;					// 메모
	
	@JsonProperty private Timestamp updateTime;
	@JsonProperty private Date insertTime;
	
	@JsonProperty private String img;						// 이미지
	
	
	public Partner() {}
	public Partner(Long id) { this.id = id; }
	
	
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
	

	@Columns(columns={
			@Column(name="biz_num1", nullable=true),
			@Column(name="biz_num2", nullable=true),
			@Column(name="biz_num3", nullable=true),
	})
	@Type(type="kr.co.koreanmagic.hibernate3.mapper.customtype.BusinessNumberCompositeUserType")
	public ThreeNumber getBizNum() {
		return bizNum;
	}
	public void setBizNum(ThreeNumber bizNum) {
		this.bizNum = bizNum;
	}

	/* 업태 */
	@Column(name="biz_condition", nullable=true)
	public String getBizCondition() {
		return bizCondition;
	}
	public void setBizCondition(String bizCondition) {
		this.bizCondition = bizCondition;
	}

	/* 종목 */
	@Column(name="biz_types", nullable=true)
	public String getBizTypes() {
		return bizTypes;
	}
	public void setBizTypes(String bizTypes) {
		this.bizTypes = bizTypes;
	}

	@Column(nullable=false)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	
	@Column(name="ceo_name")
	public String getCeoName() {
		return ceoName;
	}
	public void setCeoName(String ceoName) {
		this.ceoName = ceoName;
	}
	
	
	// 주소
	@OneToMany(mappedBy="partner", orphanRemoval=true, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Address> getAddress() {
		return address == null ? address = new ArrayList<>() : address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public void addAddress(Address address) {
		getAddress().add(address);
		address.setPartner(this);
	}
	
	// 은행
	@OneToMany(mappedBy="partner", orphanRemoval=true, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Bank> getBanks() {
		return banks == null ? banks = new ArrayList<>() : banks;
	}
	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
	public void addBank(Bank bank) {
		getBanks().add(bank);
		bank.setPartner(this);
	}
	
	// 매니저
	@OneToMany(mappedBy="partner", orphanRemoval=true, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	public List<Manager> getManager() {
		return manager == null ? manager = new ArrayList<>() : manager;
	}
	public void setManager(List<Manager> manager) {
		this.manager = manager;
	}
	@SuppressWarnings("unchecked")
	public <T extends Partner> T addManager(Manager manager) {
		getManager().add(manager);
		manager.setPartner(this);
		return (T)this;
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

	
	public Email getEmail() {
		return email == null ? email = new Email() : email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}

	
	
	
	public Website getWeb() {
		return web == null ? web = new Website() : web;
	}
	public void setWeb(Website web) {
		this.web = web;
	}
	
	
	public Webhard getWebhard() {
		return webhard == null ? webhard = new Webhard() : webhard;
	} 
	public void setWebhard(Webhard webhard) {
		this.webhard = webhard;
	}
	
	
	
	@Column(name="update_time", insertable=false, updatable=false,
				columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
	@Column(name="insert_time", nullable=false, updatable=false) 
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
		if (getClass() != obj.getClass())
			return false;
		Partner other = (Partner) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return getName();
	}
	
	

}

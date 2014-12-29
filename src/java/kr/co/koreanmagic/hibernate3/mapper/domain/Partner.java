package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.BizClass;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype.CompanyType;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorOptions(force=true)
public abstract class Partner {
	
	
	private Long id;
	private Long version;
	
	private CompanyType companyType;		// 사업자 종류
	private ThreeNumber bizNum;				// 사업자번호
	private BizClass bizClass;				// # 업태
	private String bizTypes;				// 종목
	
	private String name;				// 회사 이름
	private String ceoName;					// 사업주 이름
	private ThreeNumber mobile;				// 사업주 핸드폰
	private ThreeNumber tel;				// 대표전화
	private ThreeNumber fax;				// 대표팩스
	private Email email;					// 대표 이메일
	
	private Collection<Address> address
					= new ArrayList<>();	// # 주소
	
	private String memo;					// 메모
	
	private Timestamp updateTime;
	private Date insertTime;

	
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
	
	@Column(name="company_type", nullable=true)
	public CompanyType getCompanyType() {
		return companyType;
	}
	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
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
	@Index(name="biz_class_index")
	@JoinColumn(name="biz_class")
	@ManyToOne(optional=true)
	public BizClass getBizClass() {
		//if(bizClass == null) bizClass = new BizClass();
		return bizClass;
	}
	public void setBizClass(BizClass bizClass) {
		this.bizClass = bizClass;
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

	@Column(name="ceo_name")
	public String getCeoName() {
		return ceoName;
	}
	public void setCeoName(String ceoName) {
		this.ceoName = ceoName;
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
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}

	@OneToMany(mappedBy="partner", orphanRemoval=true)
	@Cascade(CascadeType.SAVE_UPDATE)
	public Collection<Address> getAddress() {
		return address;
	}
	public void setAddress(Collection<Address> address) {
		this.address = address;
	}
	public void addAddress(Address address) {
		getAddress().add(address);
		address.setPartner(this);
	}

	
	@Column(name="update_time", columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="insert_time", nullable=false) 
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

package kr.co.koreanmagic.hibernate3.mapper.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.ZipCode;

@Entity
@Table(name="addresses")
public class Address {
	
	private Long id;
	
	private ZipCode zipCode;
	private String address;
	private String detail = "";
	
	private Partner partner;
	
	
	public Address() {}
	public Address(ZipCode zipCode, String address, String detail) {
		setZipCode(zipCode);
		setAddress(address);
		setDetail(detail);
	}
	public Address(String address, String detail) {
		setAddress(address);
		setDetail(detail);
	}
	public Address(String address) {
		setAddress(address);
	}
	
	
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	public ZipCode getZipCode() {
		if(zipCode == null) zipCode = new ZipCode();
		return zipCode;
	}
	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}
	
	@Column(nullable=false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(nullable=false)
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Override
	public String toString() {
		String zipCode = getZipCode().toString();
		zipCode = zipCode.length() == 0 ? zipCode : "[" + zipCode + "] "; 
		return zipCode + getAddress() + " " +getDetail();
	}

}

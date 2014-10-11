package kr.co.koreanmagic.hibernate.mapper.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import kr.co.koreanmagic.hibernate.mapper.domain.BankName;

@Embeddable
public class Bank {
	
	private BankName bankName;
	private String bankNum;
	private String holder;
	
	public Bank() {}
	public Bank(BankName bankName, String bankNum, String holder) {
		setBankName(bankName);
		setBankNum(bankNum);
		setHolder(holder);
	}
	public Bank(String bankNum, String holder) {
		setBankNum(bankNum);
		setHolder(holder);
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="bank_name_id")
	@ForeignKey(name="fk_bank")
	public BankName getBankName() {
		return bankName;
	}
	public void setBankName(BankName bankNmae) {
		this.bankName = bankNmae;
	}

	@Column(name="bank_num")
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankNum == null) ? 0 : bankNum.hashCode());
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
		Bank other = (Bank) obj;
		if (bankNum == null) {
			if (other.bankNum != null)
				return false;
		} else if (!bankNum.equals(other.bankNum))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("[%s] %s (%s)", getBankName(), getBankNum(), getHolder());
	}

	
}

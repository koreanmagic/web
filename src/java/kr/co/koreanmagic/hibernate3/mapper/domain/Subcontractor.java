package kr.co.koreanmagic.hibernate3.mapper.domain;

import static kr.co.koreanmagic.commons.StringUtils.nullValue;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="subcontractors")
public class Subcontractor extends Partner {
	
	public Subcontractor(){}
	public Subcontractor(Long id){ super(id); }
	
	@Override
	public String toString() {
		return String.format("[%s / %s / %s / %s]", getName(), nullValue(getMobile(), ""), nullValue(getTel(), ""), nullValue(getFax(), ""));
	}
	
}

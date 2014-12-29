package kr.co.koreanmagic.hibernate3.mapper.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/* 참고파일 */
@Entity
@Table(name="work_reference")
public class WorkReference extends WorkResource {

	private Work work;
	
	@ManyToOne
	@JoinColumn(name="work_id", insertable=false, updatable=false)
    public Work getWork() {
		return work;
	}
	public void setWork(Work work) {
		this.work = work;
		getId().setWork(work);
	}
}

package kr.co.koreanmagic.hibernate3.mapper.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="work_draft_files")
public class WorkDraftFile extends WorkFile {

	private Work work;				// 작업
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	public Work getWork() {
		return work;
	}
	public void setWork(Work work) {
		this.work = work;
	}
}

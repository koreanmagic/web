package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@GenericGenerator(name="seq_id", strategy="kr.co.koreanmagic.hibernate3.mapper.generate.CustomerSequence")
@Entity
@Table(name="work_group")
public class WorkGroup {
	
	private String id;				// 아이디
	private String subject;			// 게시물제목
	private Customer customer;		// 거래처
	private WorkState state;		// 작업상황
	
    private Date insertTime;		// 등록일
    private Boolean readCheck;		// 읽음표시
    private Integer readCount;		// 조회수

    private String memo;			// 메모
    
    
    private Collection<Work> work = new ArrayList<>(); // 작업물
    
    @OneToMany(mappedBy="workGroup", orphanRemoval=true)
    @Cascade(CascadeType.SAVE_UPDATE)
    public Collection<Work> getWork() {
		return work;
	}
    public void addWork(Work work) {
    	getWork().add(work);
    	work.setWorkGroup(this);
    }
	public void setWork(Collection<Work> work) {
		this.work = work;
	}
	
	
	public WorkGroup() {}
    public WorkGroup(WorkState state) {
    	setState(state);
    }
    
    @Id @GeneratedValue(generator="seq_id")
    @Column(columnDefinition="VARCHAR(9)", nullable=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public WorkState getState() {
		return state;
	}
	public void setState(WorkState state) {
		this.state = state;
	}
	
	
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	public Boolean getReadCheck() {
		return readCheck;
	}
	public void setReadCheck(Boolean readCheck) {
		this.readCheck = readCheck;
	}
	
	public Integer getReadCount() {
		return readCount;
	}
	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return getSubject();
	}

}

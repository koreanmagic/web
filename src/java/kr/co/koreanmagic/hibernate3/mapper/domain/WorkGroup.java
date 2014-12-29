package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.commons.KoDateUtils;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/*
 * @NOT NULL
 * subject, customer, state 
 */
@GenericGenerator(name="seq_id", strategy="kr.co.koreanmagic.hibernate3.mapper.generate.CustomerSequence")
@Entity
@Table(name="work_group")
public class WorkGroup {
	
	private String id;				// 아이디
	private Long version;
	
	private String subject;			// 게시물제목
	private Customer customer;		// 거래처
	private WorkState state;		// 작업상황
	
    private Date insertTime;		// 등록일
    private Date deliveryDate;		// 납품일

    private String tag;				// 태그 :: 검색시 사용 키워드
    
    
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
	
	@Version
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Column(nullable=false)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="customer")
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="state")
	public WorkState getState() {
		return state;
	}
	public void setState(WorkState state) {
		this.state = state;
	}
	
	
	@Column(name="insert_time", nullable=false)
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	@Column(name="delivery_time", nullable=false)
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	
	@Override
	public String toString() {
		return String.format(
								"[%s | %s] (%s-%s) %s",
								getId(),
								getCustomer().getName(),
								KoDateUtils.getSqlStyle(getInsertTime()),
								getState().getName(),
								getSubject());
	}

}

package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;

@Entity
@Table(name="hancome_work_back_up")
public class WorkBackUp {

private WorkState workState;
	
	private String id;
	
	private WorkType workType;
	private Customer customer;
	
	private String item;
	private String itemDetail;				// 품목 상세
	
	private Date insertTime;				// 등록일자
	private Timestamp updateTime;			// 수정일자
	
	private Subcontractor subcontractor;
	
	private Integer cost;
	private Integer price;
	
	private String size;
	private String memo;				// 부가적인 메모
	
	
	@ManyToOne(optional=false)
	@JoinColumn(name="state")
	public WorkState getWorkState() {
		return workState;
	}
	public void setWorkState(WorkState workState) {
		this.workState = workState;
	}

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(name="work_type")
	public WorkType getWorkType() {
		return workType;
	}
	public void setWorkType(WorkType workType) {
		this.workType = workType;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}

	@Column(name="item_detail")
	public String getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(String itemDetail) {
		this.itemDetail = itemDetail;
	}
	
	@Column(name="insert_time", nullable=true, updatable=false)
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name="update_time", nullable=true, updatable=false)
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	public Subcontractor getSubcontractor() {
		return subcontractor;
	}
	public void setSubcontractor(Subcontractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", getId(), getItem());
	}
}

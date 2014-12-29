package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Size;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype.WorkStatus;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/*
 * @Not Null
 * itemCategory, subconstractor
 */
@Entity
@Table(name="works")
public class Work {
	
	private WorkStatus workStatus;
	
	private Long id;
	private Long version;
	
	private WorkGroup workGroup;
	private ItemCategory itemCategory;		// 품목
	private String itemDetail;				// 품목 상세
	private String subject;
	
	
	private Date insertTime;				// Auto
	private Timestamp updateTime;			// Auto
	
	private Subcontractor subcontractor;
	
	private Integer cost;
	private Integer price;
	
	private Size size;
	private Size bleedSize;
	private String unit;
	

	private Integer count;		// 건수
	private String number;		// 수량
	
	
	private Date deliveryDate;
	private Address deliveryAddress;
	private String deliveryMemo;
	
	private String memo;				// 부가적인 메모
	
	
	private Collection<WorkFile> workFiles;
	private Collection<WorkDraft> workDrafts;
	private Collection<WorkReference> workReferences;
	
	
	@Column(name="work_status")
	public WorkStatus getWorkStatus() {
		if(workStatus == null) workStatus = WorkStatus.defaultType();
		return workStatus;
	}
	public void setWorkStatus(WorkStatus workStatus) {
		this.workStatus = workStatus;
	}
	
	
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
	
	@ManyToOne
	@JoinColumn(name="work_group")
	public WorkGroup getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(WorkGroup workGroup) {
		this.workGroup = workGroup;
	}

	
	@ManyToOne
	@JoinColumn(name="item_category")
	public ItemCategory getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	@Column(name="item_detail")
	public String getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(String itemDetail) {
		this.itemDetail = itemDetail;
	}
	
	
	@Column(name="subject", nullable=false)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name="insert_time", nullable=false)
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name="update_time", columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne
	@JoinColumn(name="subconstractor")
	public Subcontractor getSubconstractor() {
		return subcontractor;
	}
	public void setSubconstractor(Subcontractor subcontractor) {
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

	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	
	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="bleed_width")),
		@AttributeOverride(name="height", column=@Column(name="bleed_height")),
	})
	@Column(name="bleed_size")
	public Size getBleedSize() {
		return bleedSize;
	}
	public void setBleedSize(Size bleedSize) {
		this.bleedSize = bleedSize;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name="delivery_date")
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@ManyToOne(optional=true)
	@org.hibernate.annotations.Cascade({CascadeType.SAVE_UPDATE})
	@JoinColumn(name="delivery_address")
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	@Column(name="delivery_memo")
	public String getDeliveryMemo() {
		return deliveryMemo;
	}
	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
	/* 인쇄용 파일 */
	@OneToMany(mappedBy="work", fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.SAVE_UPDATE)
	public Collection<WorkFile> getWorkFiles() {
		return workFiles;
	}
	public void addWorkFile(WorkFile workFile) {
		getWorkFiles().add(workFile);
		workFile.setWork(this);
	}
	
	/* 시안 파일 */
	@OneToMany(mappedBy="work", fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.SAVE_UPDATE)
	public Collection<WorkDraft> getWorkDrafts() {
		if(this.workDrafts == null) this.workDrafts = new ArrayList<>();
		return workDrafts;
	}
	public void addWorkDraft(WorkDraft workDraft) {
		getWorkDrafts().add(workDraft);
		workDraft.setWork(this);
	}
	
	/* 참고 파일 */
	@OneToMany(mappedBy="work", fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.SAVE_UPDATE)
	public Collection<WorkReference> getWorkReferences() {
		if(this.workReferences == null) this.workReferences = new ArrayList<>();
		return workReferences;
	}
	public void addWorkReferences(WorkReference workReference) {
		getWorkReferences().add(workReference);
		workReference.setWork(this);
	}
	
	
	
	
	public void setWorkFiles(Collection<WorkFile> workFiles) {
		this.workFiles = workFiles;
	}
	public void setWorkDrafts(Collection<WorkDraft> workDrafts) {
		this.workDrafts = workDrafts;
	}
	public void setWorkReferences(Collection<WorkReference> workReferences) {
		this.workReferences = workReferences;
	}
	@Override
	public String toString() {
		return getInsertTime().toString();
	}
	

}

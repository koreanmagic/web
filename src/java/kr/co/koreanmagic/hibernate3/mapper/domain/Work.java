package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.DeliveryType;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
				getterVisibility = Visibility.NONE, 
				setterVisibility = Visibility.NONE)

@GenericGenerator(name="seq_id", strategy="kr.co.koreanmagic.hibernate3.mapper.generate.WorkSequence")
@Entity
@Table(name="hancome_work")
public class Work {
	
	private WorkState workState;
	
	@JsonProperty private String id;
	@JsonProperty private Long version;
	
	@JsonProperty private WorkType workType;				// 긴급, 당일판
	private Customer customer;				// 고객
	@JsonProperty private Manager manager;				// 담당자

	@JsonProperty private String item;					// 품목
	@JsonProperty private String itemDetail;				// 품목 상세
	@JsonProperty private String afterProcess;			// 후가공
	
	@JsonProperty private Date insertTime;				// 등록일자
	@JsonProperty private Timestamp updateTime;		// 수정일자
	private Date stateTime;							// 작업 상황 변경시간
	
	@JsonProperty private Subcontractor subcontractor;
	
	@JsonProperty private String count;					// 갯수
	@JsonProperty private String num;					// 건수
	@JsonProperty private Integer cost;					// 제작단가
	@JsonProperty private Integer price;					// 공급단가
	
	@JsonProperty private String size;
	@JsonProperty private String memo;				// 부가적인 메모
	
	@JsonProperty private DeliveryType delivery;		// 납품방법
	private Address address;			// 배송지
	
	private List<WorkResourceFile> resourceFile;	// 참고파일
	
	
	@ManyToOne(optional=false)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="state")
	public WorkState getWorkState() {
		return workState;
	}
	public void setWorkState(WorkState workState) {
		this.workState = workState;
	}

	@Id @GeneratedValue(generator="seq_id")
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
	
	@ManyToOne(optional=true)
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
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
	
	public String getAfterProcess() {
		return afterProcess;
	}
	public void setAfterProcess(String afterProcess) {
		this.afterProcess = afterProcess;
	}
	
	
	@Column(name="insert_time", nullable=false, updatable=false)
	public Date getInsertTime() {
		if(insertTime == null) insertTime = Date.from(Instant.now());
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name="update_time", insertable=false, updatable=false,
			columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	

	public Date getStateTime() {
		return stateTime;
	}
	public void setStateTime(Date stateTime) {
		this.stateTime = stateTime;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	public Subcontractor getSubcontractor() {
		return subcontractor;
	}
	public void setSubcontractor(Subcontractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
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
	
	
	
	public DeliveryType getDelivery() {
		return delivery;
	}
	public void setDelivery(DeliveryType delivery) {
		this.delivery = delivery;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	// 리소스 파일
	@OneToMany(fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL, mappedBy="work")
	public List<WorkResourceFile> getResourceFile() {
		if(resourceFile == null) resourceFile = new ArrayList<>();
		return resourceFile;
	}
	public void setResourceFile(List<?> resourceFile) {
		this.resourceFile = (List<WorkResourceFile>)resourceFile;
	}
	public<T extends WorkFile> void addResourceFile(T resourceFile) {
		resourceFile.setWork(this);
		getResourceFile().add((WorkResourceFile)resourceFile);
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", getId(), getItem());
	}
	

}

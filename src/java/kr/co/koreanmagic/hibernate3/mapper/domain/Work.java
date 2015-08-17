package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.DeliveryType;
import kr.co.koreanmagic.hibernate3.mapper.domain.enumtype.WorkType;
import kr.co.koreanmagic.hibernate3.mapper.generate.WorkSequence;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;


@JsonAutoDetect(fieldVisibility=Visibility.NONE, 
				getterVisibility = Visibility.NONE, 
				setterVisibility = Visibility.NONE)

@GenericGenerator(name="seq_id", strategy="kr.co.koreanmagic.hibernate3.mapper.generate.WorkSequence")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Entity
@Table(name="hancome_work")
public class Work {
	
	private static Logger logger = Logger.getLogger(Work.class);
	
	private static final Path RESOURCE_PATH; 
	
	static {
		try {
			Properties p = new Properties();
			p.load(WorkSequence.class.getClassLoader().getResourceAsStream("resource.properties"));
			RESOURCE_PATH = Paths.get( p.get("hancome.workfile.root").toString() );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		logger.debug("로딩완료 :: " + RESOURCE_PATH);
		
	}
	
	private WorkState workState;
	
	@JsonProperty private String id;
	@JsonProperty private Long version;
	
	@JsonProperty private WorkType workType;			// 긴급, 당일판
	private Customer customer;				// 고객
	@JsonProperty private Manager manager;				// 담당자

	@JsonProperty private String item;					// 품목
	@JsonProperty private String itemDetail;			// 품목 상세
	@JsonProperty private String afterProcess;		// 후가공
	
	@JsonProperty private Date insertTime;				// 등록일자
	@JsonProperty private Timestamp updateTime;		// 수정일자
	private Date stateTime;									// 작업 상황 변경시간
	
	@JsonProperty private Subcontractor subcontractor;
	
	@JsonProperty private String count;					// 갯수
	@JsonProperty private String num;					// 건수
	@JsonProperty private Integer cost;					// 제작단가
	@JsonProperty private Integer price;				// 공급단가
	
	@JsonProperty private String size;
	@JsonProperty private String memo;					// 부가적인 메모
	
	@JsonProperty private DeliveryType delivery;		// 납품방법
	private Address address;								// 배송지
	

	
	
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

	
	private List<WorkResourceFile> resourceFile = new ArrayList<>();	// 참고파일
	private List<WorkConfirmFile> confirmFile = new ArrayList<>();		// 인쇄
	private List<WorkDraftFile> draftFile = new ArrayList<>();		// 시안
	
	// 리소스 파일
	@OneToMany(fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL, mappedBy="work")
	public List<WorkResourceFile> getResourceFile() {
		return resourceFile;
	}
	public void setResourceFile(List<WorkResourceFile> resourceFile) {
		this.resourceFile = resourceFile;
	}
	public void addResourceFile(WorkResourceFile resourceFile) {
		resourceFile.setWork(this);
		getResourceFile().add(resourceFile);
	}
	
	// 시안 파일
	@OneToMany(fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL, mappedBy="work")
	public List<WorkDraftFile> getDraftFile() {
		return draftFile;
	}
	public void setDraftFile(List<WorkDraftFile> draftFile) {
		this.draftFile = draftFile;
	}
	public void addDraftFile(WorkDraftFile draftFile) {
		draftFile.setWork(this);
		getDraftFile().add(draftFile);
	}
	
	
	// 인쇄
	@OneToMany(fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL, mappedBy="work", targetEntity=WorkConfirmFile.class)
	public List<WorkConfirmFile> getConfirmFile() {
		return confirmFile;
	}
	public void setConfirmFile(List<WorkConfirmFile> confirmFile) {
		this.confirmFile = confirmFile;
	}
	public void addConfirmFile(WorkConfirmFile confirmFile) {
		confirmFile.setWork(this);
		this.confirmFile.add(confirmFile);
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", getId(), getItem());
	}
	
	//150522002
	@Transient
	public final String savePath() {
		// 150522002 ==> 2015-05/02
		return "20" + getId().substring(0, 2) + "-" + getId().substring(2, 4) 
					+ "/" + getId().substring(4, 6)
					+ "/" + getId().substring(6, 9);
	}
	
	// 채워져 들어오는 항목 :: orignalName, fileType, size
	public final void saveFile(InputStream is, WorkFile file) {
		if(file instanceof WorkResourceFile) fill(is, file, "files", false);
		else if (file instanceof WorkDraftFile) fill(is, file, "imgs", true);
		else if (file instanceof WorkConfirmFile) fill(is, file, "", false);
		else throw new RuntimeException("올바른 WorkFile객체가 아닙니다.");
	}
	
	private final void fill(InputStream is, WorkFile file, String subPath, boolean thumb) {
		
		subPath = subPath.length() > 0 ? "/" + subPath : "";
		
		file.setParentPath( savePath() + subPath );
		file.setSaveName( uuid( file.getOriginalName() ) );
		
		save(is, RESOURCE_PATH
						.resolve(file.getParentPath())
						.resolve( String.join(".", file.getSaveName(), file.getFileType()) )
						, thumb
		);
	}
	
	// 파일저장
	private final void save(InputStream is, Path target, boolean thumb) {
		try {
			Files.createDirectories(target.getParent());
			Files.copy(is, target);
			System.out.println(target);
			// 썸네일 이미지 생성
			if(thumb) {
				Thumbnails.of(target.toFile())
					.height(150)
					.outputFormat("jpg")
					.toFiles(Rename.SUFFIX_HYPHEN_THUMBNAIL);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// 파일명-a400ca.type
	private String uuid(String fileName) {
		return fileName + uuid(); 
	}
	// 유일키 생성
	// 아주 만약에 중복된 값이 나올 수 있다. 중복된 값이 나오면 로또 긁으러 가자
	private String uuid() {
		String uuid = UUID.randomUUID().toString();
		return "-" + uuid.substring(0, uuid.indexOf("-"));
	}

}

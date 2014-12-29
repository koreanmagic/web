package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="work_resource")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class WorkResource implements Comparable<WorkResource> {
	
	private FileId id;	// 아이디
	
	private Date insertTime;
    private Timestamp uploadTime;		// 업로드 타임

	private String path;				// 파일 패스
    private String name;				// 파일명
    private String type;				// 파일 확장자
    private Long size;					// 파일크기
    
    private Boolean isUse;				// 유효플래그
    private Integer downloadCount;		// 다운로드 횟수
    
    private String memo;				// 메모
    
    public WorkResource() {}
    
    @EmbeddedId
	public FileId getId() {
    	if(id == null) id = new FileId();
		return id;
	}
	public void setId(FileId id) {
		this.id = id;
	}
	
	
	@Column(columnDefinition="DATETIME NOT NULL", nullable=false)
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	
	@Column(columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Boolean getIsUse() {
		return isUse;
	}
	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}
	
	
	public Integer getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}
    
    @Override
    public String toString() {
    	return String.format("%s.%s", getName(), getType());
    }
    
	@Override
	public int compareTo(WorkResource o) {
		Timestamp current = this.uploadTime;
		Timestamp other = o.uploadTime;
		return current.compareTo(other);
	}
    
	
	@Embeddable
	public static class FileId implements Serializable {
		
		private Long id;
		private Long work;
		
		public FileId() {}
		
		public FileId(Work work) {
			this.work = work.getId();
		}
		
		@Column(name="id", nullable=false, columnDefinition="BIGINT(20) NOT NULL AUTO_INCREMENT")
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		@Column(name="work_id")
		public Long getWork() {
			return work;
		}
		public void setWork(Work work) {
			this.work = work.getId();
		}
		
	}
    

}


package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.beans.Transient;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="hancome_work_files")
public abstract class WorkFile {
	
	@JsonProperty private Long id;
	
	@JsonProperty private Date uploadTime;		// 업로드 시간

	// 파일정보
	@JsonProperty private String originalName;		// 업로드할때 이름
	@JsonProperty private String saveName;		// 저장할때 이름
	@JsonProperty private String parentPath;		// 부모경로 (저장 루트경로는 동적으로 결정된다.)
	@JsonProperty private Integer size;			// 파일 사이즈
	@JsonProperty private String fileType;		// 파일 종류
	@JsonProperty private String memo;			// 파일 메모
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public abstract void setWork(Work work);
	
	@Column(nullable=false, updatable=false) 
	public Date getUploadTime() {
		if(uploadTime == null) uploadTime = Date.from(Instant.now());
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	@Column(nullable=false)
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@Column(nullable=true)
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	
	@Column(nullable=true)
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Column(nullable=true)
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Column(nullable=true)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Transient
	public void setFile(String fileName) {
		int pos = fileName.lastIndexOf(".");
		setOriginalName(fileName.substring(0, pos));
		setFileType(fileName.substring(pos + 1, fileName.length()));
	}
	
	@Override
	public String toString() {
		return getOriginalName() + "." + getFileType();
	}
	

}

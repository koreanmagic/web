package kr.co.koreanmagic.hibernate3.legacy.work.files;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="work_files")
public class FileBean {

	
	private Integer id;
	private String memo; 

	@Column(name="upload_time") private Timestamp uploadTime;
	@Column(name="work_id") private String workId;
	@Column(name="file_type") private String fileType;
	@Column(name="parent_path") private String parentPath;
	@Column(name="file_name") private String fileName;
    @Transient @Column(name="is_use") private Boolean isUse;
    @Column(name="download_count") private Integer downloadCount;
    @Column(name="file_size") private Integer fileSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId == null ? null : workId.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath == null ? null : parentPath.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    
    public void setFullFileName(String fileName) {
    	int dot = fileName.lastIndexOf(".");
    	setFileName(fileName.substring(0, dot));
    	setFileType(fileName.substring(dot+1, fileName.length()));
    }
    
    public String toFileName() {
    	return getFileName() + "." + getFileType();
    }
    
    @Override
    public String toString() {
    	return getParentPath() + "/" + getFileName() + "." + getFileType();
    }
}

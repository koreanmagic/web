package kr.co.koreanmagic.web.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

import kr.co.koreanmagic.commons.KoFileUtils;
import kr.co.koreanmagic.web.db.mybatis.mapper.extension.WorkDataMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkData;

import org.jboss.logging.Logger;
import org.springframework.transaction.annotation.Transactional;

public abstract class ResourceDownloader<T extends WorkData> implements GeneralService<T, Integer>  {

	private final Logger logger = Logger.getLogger(getClass());
	private WorkDataMapper<T> mapper;

	protected ResourceDownloader(WorkDataMapper<T> mapper) {
		this.mapper = mapper;
	}
	protected WorkDataMapper<T> getMapper() { return mapper; }
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ interface define ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	// 해당 work_id의 리소스 파일 갯수
	public final int count(Work work) {
		return getMapper().count(work.getId());
	}
	// 다운로드 카운터 올리기
	public final int addCount(Integer id) {
		return getMapper().addCount(id);
	}
	// [never used] 업데이트
	@Override
	public final int update(T domain) {
		return getMapper().updateByPrimaryKeySelective(domain);
	}
	// 목록 삽입
	@Override
	public final int insert(T domain) {
		return getMapper().insertSelective(domain);
	}
	// 리소스 아이디로 리소스 가지고 오기
	@Override
	public final T get(Integer id) {
		return getMapper().selectByPrimaryKey(id);
	}
	
	public List<T> get(Work work) {
		return nullValueCheck(getMapper().selectByWorkId(work.getId()));
	}
	
	@Override
	public int delete(T domain) {
		return 0;
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ interface define ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ global method ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	@Transactional
	public int insert(List<? extends WorkData> list) {
		int count = 0;
		for(WorkData l : list) {
			getMapper().insertSelective(l);
			count++;
		}
		return count;
	}	
	
	
	@SuppressWarnings("unchecked")
	protected List<T> nullValueCheck(List<T> list) {
		return list == null ? Collections.EMPTY_LIST : list;
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ global method ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ fileDownload ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	abstract WorkData getWorkData();
	abstract String getDownloadFileName(Work work, String originalFileName); // 확장자 제외한 파일명 결정
	
	/*
	 * InputStream is : 다운로드할 파일 스트림
	 * Path root : 루트 폴더 (상황에 따라 바뀔 수 있다.)
	 * Path parentPath : 루트 폴더 이하 경로. DB에 그대로 저장이 된다.
	 */
	public WorkData fileDownload(Work work, InputStream is, Path root, Path parentPath, String fileName) throws IOException {
		
		WorkData workData = getWorkData();
		
		workData.setWorkId(work.getId()); 										// ▼ 아이디
		workData.setFileType(KoFileUtils.getFileType(fileName));				// ▼ 파일확장자
		workData.setParentPath(parentPath.toString().replace("\\", "/")); 		// ▼ 부모 폴더
		
		String[] separateFileName = KoFileUtils.separateFileName(fileName);
		String newFileName = getDownloadFileName(
								work, separateFileName[0]
							);	
		
		// 중복파일 확인
		Path realPath = root.resolve(parentPath.resolve(newFileName + "." + separateFileName[1]));
		realPath = KoFileUtils.overlapNumbering(realPath, true);
		
		workData.setFileName(
					KoFileUtils.separateFileName(realPath.getFileName().toString())[0]		// ▼ 파일명
				);		
		
		int fileSize = download(is,
								Files.newOutputStream(realPath, StandardOpenOption.TRUNCATE_EXISTING),
								8912);
		
		workData.setFileSize(fileSize);											// ▼ 파일 사이즈
		
		return workData;
	}
	
	
	// 파일 다운로드 핵심 메서드
	protected int download(InputStream _from, OutputStream _to, int bufSize) throws IOException {
		byte[] buf = new byte[bufSize];
		int len = 0;
		int read = -1;
		try (InputStream from = _from; OutputStream to = _to;) {
			while ((read = from.read(buf)) != -1) {
				to.write(buf, 0, read);
				len += read;
			}
		}
		return len;
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ fileDownload ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
}

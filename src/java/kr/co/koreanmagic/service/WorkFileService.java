package kr.co.koreanmagic.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import kr.co.koreanmagic.context.support.file.FileDownloader;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkFile;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class WorkFileService<T extends WorkFile> extends GenericService<T, Long> {
	
	@Override
	public T getInitalBean() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Transactional
	public T getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	@Transactional(readOnly=true)
	public int getSize(Work work) {
		return Integer.parseInt(
					getDao().criteria()
							.setProjection(Projections.rowCount())
							.add(Restrictions.eq("work", work))
							.uniqueResult()
							.toString()
				);
	}
	
	
	// 작업에 해당하는 리스트 가지고 오기
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public List<T> getList(Work work) {
		return (List<T>)getDao().criteria()
							.add(Restrictions.eq("work", work))
							.list();
	}
	
	
	// Ajax용 리스트 
	/*@Transactional
	public List<Map<String, String>> getValueListMap(Work work, String[] props) {
		props = props == null ?
				new String[]{ "id", "orignalName", "saveName", "size", "fileType", "memo", "uploadTime", "parentPath" } :
				props;
		
		return super.getValueListMap(getList(work),
									props,
									null);
	}*/
	
	// 아이디 받아 지우기
	public void delete(Long id) {
		getDao().makeTransient( getDao().findById(id) );
	}
	
	// ★★★★★  파일 저장  ★★★★★
	@Transactional
	public Work saveFile(List<MultipartFile> files, Work work) {
		T workFile = null;
		Path path = null;
		List<? super WorkFile> list = new ArrayList<>();
		
		try {
			
			for(MultipartFile file : files) {
				workFile = createBean(work, (int)file.getSize(), file.getOriginalFilename(), file.getContentType() );
				list.add(workFile);
				path = getDownloader().saveFile(file.getInputStream(), file.getOriginalFilename());
				workFile.setSaveName( path.getFileName().toString() );
				workFile.setParentPath( path.getParent().toString().replace("\\\\", "/") );
				
				// 데이터베이스에 저장한다.
				add(workFile);
			}
			
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
		
		return null;
		//work.setResourceFile(list);
		//return work;
	}
	
	abstract FileDownloader getDownloader();
	
	// 객체 생성
	private T createBean(Work work, Integer size, String orignalFileName, String contentType) {
		T file = getInitalBean();
		file.setFile(orignalFileName);
		file.setSize(size);
		file.setWork(work);
		return file;
	}

}

package kr.co.koreanmagic.service;

import java.io.IOException;
import java.util.List;

import kr.co.koreanmagic.hibernate3.스프링_하이버네이트_테스트;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkFile;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class WorkFileService<T extends WorkFile> extends GenericService<T, Long> {
	
	
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
	
	
	// 아이디 받아 지우기
	public void delete(Long id) {
		getDao().makeTransient( getDao().findById(id) );
	}
	
	
	
	public void saveFile(List<MultipartFile> files, Work work) {
		for(MultipartFile file : files) {
			saveFile(file, work);
		}
	}
	// ★★★★★  파일 저장  ★★★★★
	@Transactional
	public T saveFile(MultipartFile file, Work work) {
		
		T workFile = createBean(work, (int)file.getSize(), file.getOriginalFilename() );
		
		try {
			work.saveFile(file.getInputStream(), workFile);
			add(workFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return workFile;
	}
	
	// 객체 생성
	private T createBean(Work work, Integer size, String orignalFileName) {
		T file = getInitalBean();
		
		file.setFile(orignalFileName);
		file.setSize(size);
		file.setWork(work);
		return file;
	}

}

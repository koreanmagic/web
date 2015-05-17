package kr.co.koreanmagic.hibernate3.legacy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import kr.co.koreanmagic.hibernate3.legacy.creator.CreatCustomer;
import kr.co.koreanmagic.hibernate3.legacy.creator.CreatSubcontractor;
import kr.co.koreanmagic.hibernate3.legacy.creator.CreatWork;
import kr.co.koreanmagic.hibernate3.legacy.support.AbstractPersistentCreator;
import kr.co.koreanmagic.hibernate3.legacy.support.CSVManager;
import kr.co.koreanmagic.hibernate3.legacy.support.NameConvertManager;
import kr.co.koreanmagic.hibernate3.legacy.support.NameConvertor;
import kr.co.koreanmagic.hibernate3.legacy.support.NoSuchMasterNameException;
import kr.co.koreanmagic.hibernate3.legacy.work.files.FileBean;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Banner;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Card;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Design;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Envelope;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Goods;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Invitation;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.MasterPrinting;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.PagePrint;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Print;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Sign;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Sticker;
import kr.co.koreanmagic.hibernate3.support.ResultTransformerUtil;
import kr.co.koreanmagic.service.GenericService;
import kr.co.koreanmagic.service.SubcontractorService;

import org.hibernate.PropertyValueException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@ComponentScan({
	"kr.co.koreanmagic.hibernate3.legacy"
})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={이전작업Configuration.class})
public class DB이전작업 extends AbstarctDB이전작업 {

	
	@Inject Provider<CreatCustomer> createCustomer;
	@Inject Provider<CreatSubcontractor> creatSubcontractor;
	@Inject Provider<CreatWork> creatWork;
	@Autowired SubcontractorService service;
	
	static final String[] WORKLIST_COLUMNS = {
		"id", "insert_time", "con_id", "customer", "item", "item_type", "count", "number", "size", "bleed",
		"unit", "tag", "cost", "price", "update_time", "delivery", "delivery_time", "subcontract", "read_count",
		"bleed_size", "read_check"
	};
	
	@Test
	@Transactional
	//@Rollback(false)
	public void 리스트입력() throws Exception {
		
		Session session = session();
		SQLQuery query = session.createSQLQuery("select customer, update_time, id from hancome_work");
		List<Object[]> works = query.addScalar("customer").addScalar("update_time").addScalar("id").list();
		NameConvertor convert = NameConvertManager.get(Customer.class);
		
		query = session.createSQLQuery("update hancome_work set customer = :c, update_time = :u WHERE id = :i");
		
		for(Object[] work : works) {
			try {
				log(work[0]);
				log(convert.convert(work[0].toString()));
				query.setParameter("c", convert.convert(work[0].toString())).setParameter("u", work[1]).setParameter("i", work[2]);
				log(query.executeUpdate());
			} catch ( NoSuchMasterNameException e ) {
				log(e.getName());
			}
		}
		
		
	}
	
	private void 이름리스트확인(Class<?> clazz, String path) throws Exception {
		NameConvertor convert = NameConvertManager.get(clazz);
		List<String> list = Files.readAllLines(Paths.get(path));
		for(String name : list) {
			try {
				convert.convert(name);
			} catch (NoSuchMasterNameException e) {
				log(e.getName());
			}
			
		}
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 파일 관리 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * 2014-11-09 // 모든 파일명을 수정했다!!
	 * 
	 * List<FileBean> ::  레거시 테이블에 있는 모든 파일목록.
	 * Path root :: 검색할 루트 패스 ★그리고 동시에 parentPath를 산출해내는 기준이 된다. 이 점 꼭 유의하라
	 * 
	 * 이 메서드는 재귀함수이다.
	 * 하위폴더로 내려가면서 계속해서 파일을 검색한다.
	 * 
	 * 1) 이때 레거시 목록에 있는 parentPath는 무시하고 파일명만 가지고 검색을 한다.
	 * 2) 이는 레거시 파일목록에 있는 parentPath에 오류가 있기때문이다.
	 * 3) 이 parentPath를 수정하는 것이 이 작업의 목적이다.
	 * 4) 수정된 parentPath는 FileBean에 입력된 후, 최종적으로 다시 DB에 입력된다.
	 * 
	 */
	private List<FileBean> 파일찾기(Path root, List<FileBean> beans) throws Exception {
		return 파일찾기(root, root, beans);
	}
	private List<FileBean> 파일찾기(Path root, Path dir, List<FileBean> beans) throws Exception {
		
		FileBean bean = null;
		
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
			
			for(Path path : stream) {
				if(Files.isDirectory(path))		// 폴더일 경우 재귀로 돌린다.
					파일찾기(root, path, beans);
				
				else {
					bean = 파일확인(path, beans);
					if(bean != null)				// null이 아닐 경우 
						빈수정(root, path, bean);
				}
			}
		}
		return beans;
	}
	
	/*
	 *  현재 파일Path가 레거시 파일목록에 있는지 확인해본다.
	 *  존재하면 해당 FileBean을 반환하고, 아니면 null을 보낸다.
	 */
	private FileBean 파일확인(Path file, List<FileBean> beans) throws Exception {
		for(FileBean bean : beans)
			if(file.getFileName().toString().equals(bean.toFileName()))
				return bean;
		return null;
	}
	private void 빈수정(Path root, Path file, FileBean bean) {
		bean.setParentPath(searchParentPath(root, file).toString().replace("\\", "/"));
		bean.setFullFileName(file.getFileName().toString().replace("\\", "/"));
		log("modify:: " + root.toString() + "/" + bean.getParentPath() + "/" + bean.getFileName() );
	}
	
	/*
	 *  루트폴더와 파일 제외한 중간 패스 계산
	 *  WorkResource 엔터티의 parentPath에 해당하는 값을 산출해낸다.
	 */
	private Path searchParentPath(Path root, Path file) {
		int count = 1, pathCount = file.getNameCount();
		Path dir = root.getFileName();
		for(Path f : file) {
			if(f.equals(dir)) break;
			count++;
		}
		return file.subpath(count, pathCount - 1);
	}
	
	
	/*
	 * 레거시 파일목록을 모두 추출해서 실제 파일이 있는지 확인해본다.
	 * 없는 파일만 뽑아서 반환한다.
	 * 
	 * root :: 레거시 파일목록이 제공하는 (parentPath / fileName + fileType) 앞에 붙일 rootPath
	 */
	public List<FileBean> 없는파일리스트확인(Path root, Class<FileBean> clazz) throws Exception  {
		Query query = null;
		query = ResultTransformerUtil.createScala(session(), clazz);
		query.setResultTransformer(Transformers.aliasToBean(clazz));
		
		return 없는리스트추출(root, query.list());
	}
	
	private List<FileBean> 없는리스트추출(Path root, List<FileBean> targetList) {
		List<FileBean> returnValue = new ArrayList<>();
				   
		Path file = null;
		int num = 1;
		
		for(FileBean bean : targetList) {
			file = root.resolve(bean.toString());					// toString()은 전체 경로를 반환한다.
			if(!Files.exists(file)) {
				log("loss:: " + num++ + ") ["+ bean.getWorkId() + "] " + file);	// 목록에 담으면서 출력해준다.
				returnValue.add(bean);
			}
		}
		
		log("-- 종료 --");
		return returnValue;
	}
	
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 새로운 엔터티 입력 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */

	/*
	 * 데이터가 있는 dataFile Path와
	 * data를 영속화객체로 변환시키는 AbstractPersistentCreator 구현 객체를 제공한다.
	 * 
	 * dataFile : 양식에 맞는 txt 파일
	 * 
	 */
	private<T> int 입력(Path dataFile, AbstractPersistentCreator<T> creator) throws Exception {
		int count = 0;
		T entity = null;
		List<Map<String, Object>> list = CSVManager.readCSV(dataFile, "utf-8");
		
		GenericService<T, ? extends Serializable> service = creator.getService();

		try {
			for(Map<String, Object> map : list) {
				entity = creator.creat(map);
				service.add(entity);
				count++;
			}
		} catch (PropertyValueException e) {
			log(entity);
			throw e;
		}
		
		return count;
	}
	
	@Transactional
	public void 카테고리_입력() throws Exception {
		Object[] array = {
				new Object[]{Card.class, "일반명함", "수입지명함", "PP명함"},
				new Object[]{Sticker.class, "일반스티커", "특수스티커", "도무송스티커"},
				new Object[]{Print.class, "합판전단", "독판전단", "소량전단", "기타 전단"},
				new Object[]{Envelope.class, "대봉투", "소봉투", "기성봉투"},
				new Object[]{PagePrint.class, "카다로그", "리플렛/팜플렛", "책자", "신문"},
				new Object[]{MasterPrinting.class, "양식지", "NCR지", "마스터 인쇄"},
				new Object[]{Invitation.class, "티켓", "청첩장", "엽서", "상품권", "카드/명찰", "기타 품목"},
				new Object[]{Sign.class, "후렉스 간판", "채널 간판", "천갈이", "기타 간판"},
				new Object[]{Banner.class, "유포출력", "현수막", "페트천", "시트컷팅", "배너", "스카시", "기타 출력"},
				new Object[]{Goods.class, "상패류", "판촉물", "기타 판촉물"},
				new Object[]{Design.class, "CI/BI", "편집 작업", "디자인 작업", "기타 작업"},
		};
		
		for(Object value : array) {
			카테고리_입력((Object[])value);
		}
	}
	private void 카테고리_입력(Object[] values) throws Exception {
		Class<?> clazz = (Class<?>)values[0];
		Method setter = clazz.getMethod("setName", String.class);
		Object obj = null;
		
		int i = 0;
		for(Object value : values) {
			if(i++ == 0) continue;
			obj = clazz.newInstance();
			setter.invoke(obj, value.toString());
			session().save(obj);
		}
	}
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ NameConverter 갱신작업 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/* 현재 work_list에 등록된 데이터 중 확인되지 않은 이름이 있는지 확인한다. */
	/*
	 * ▲ NameConverter의 역할
	 * ★ PersisTentCreator를 돕기 위한 보조장치 ★
	 * 레거시 데이터는 모두 work_list 테이블에 혼합되어 들어있다.
	 * 이를 분석해서 새롭게 만든 엔터티들로 나누어 담아야 하는데... 회사명, 품목, 거래처명이 제멋대로 되어있다.
	 * NameConverter는 이같은 이름들을 분석해서 하나의 이름으로 수렴해주는 역할이다. 일종의 필터다.
	 * 다시 말해서 NameConverter는 PersisTentCreator를 돕기 위한 보조장치다.
	 */
	
	@Transactional
	protected void 하청업체이름확인() throws Exception {
		NameConveter확인(Subcontractor.class, "SELECT subcontract FROM work_list");
	}
	@Transactional
	protected void 아이템카테고리확인() throws Exception {
		NameConveter확인(ItemCategory.class, "SELECT item FROM work_list");
	}
	@Transactional
	protected void 거래처이름확인() throws Exception {
		NameConveter확인(Customer.class, "SELECT customer FROM work_list");
	}
	
	private void NameConveter확인(Class<?> key, String query) throws Exception {
		NameConvertor convertor = NameConvertManager.get(key);
		List<Object> obj = factory.getCurrentSession().createSQLQuery(query).list();
		
		int count = 0;
		
		for(Object v : obj) {
			try {
				convertor.convert(v.toString());
			} catch(NoSuchMasterNameException e) {
				log(e.getName());
				count++;
			}
		}
		
		log("갱신해야할 이름 갯수 : " + count);
	}
	

}

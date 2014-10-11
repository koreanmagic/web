package kr.co.koreanmagic.hibernate;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import kr.co.koreanmagic.commons.KoFileUtils;
import kr.co.koreanmagic.hibernate.legacy.CSVCustomer;
import kr.co.koreanmagic.hibernate.legacy.CSVPull;
import kr.co.koreanmagic.hibernate.legacy.CSVSubcontractor;
import kr.co.koreanmagic.hibernate.legacy.CSVWork;
import kr.co.koreanmagic.hibernate.legacy.NameConvert;
import kr.co.koreanmagic.hibernate.legacy.NameConvert.NameConvertor;
import kr.co.koreanmagic.hibernate.legacy.NoSuchMasterNameException;
import kr.co.koreanmagic.hibernate.legacy.PersistenceList;
import kr.co.koreanmagic.hibernate.mapper.category.Banner;
import kr.co.koreanmagic.hibernate.mapper.category.Card;
import kr.co.koreanmagic.hibernate.mapper.category.Design;
import kr.co.koreanmagic.hibernate.mapper.category.Envelope;
import kr.co.koreanmagic.hibernate.mapper.category.Goods;
import kr.co.koreanmagic.hibernate.mapper.category.Invitation;
import kr.co.koreanmagic.hibernate.mapper.category.ItemCategory;
import kr.co.koreanmagic.hibernate.mapper.category.MasterPrinting;
import kr.co.koreanmagic.hibernate.mapper.category.PagePrint;
import kr.co.koreanmagic.hibernate.mapper.category.Print;
import kr.co.koreanmagic.hibernate.mapper.category.Sign;
import kr.co.koreanmagic.hibernate.mapper.category.Sticker;
import kr.co.koreanmagic.hibernate.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate.mapper.domain.WorkGroup;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class __HiberNate_TEST extends TestDao {

	private static Logger logger = Logger.getLogger(__HiberNate_TEST.class);
	
	private boolean BEFORE = false;

	private static SessionFactory factory;
	private static Session session;
	private static Transaction transaction;
	private static boolean isCurrentSession;
	
	@Before
	public void init() throws Exception {
		EntityManagerFactory jtaFactory = Persistence.createEntityManagerFactory("hancome");
		HibernateEntityManagerFactory hibernateFactory = (HibernateEntityManagerFactory)jtaFactory;
		//HibernateEntityManager jtaSession = (HibernateEntityManager)jtaFactory.createEntityManager();
		
		factory = hibernateFactory.getSessionFactory();
		this.session = session();
		/*
		 * 모든 리스트 끌어올림
		 */
		/*if (BEFORE) {
			PersistenceList.cache(BankName.class, session);
			PersistenceList.cache(Customer.class, session);
			PersistenceList.cache(ItemType.class, session);
			PersistenceList.cache(BizClass.class, session);
			PersistenceList.cache(Subcontractor.class, session);
			PersistenceList.cache(ItemCategory.class, session);
		}*/
	}
	
	
	@After
	public void end() {
		log("테스트 종료");
	}
	
	
	
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
		log(setter);
		Object obj = null;
		
		int i = 0;
		for(Object value : values) {
			if(i++ == 0) continue;
			obj = clazz.newInstance();
			setter.invoke(obj, value.toString());
			session.save(obj);
		}
	}
	
	
	private Set<String> 네임리스트에_없는_이름찾기(String nameList, String target) throws Exception {
		Set<String> result = new TreeSet<>();
		NameConvertor convertor = NameConvert.getConvertorByPath(Paths.get(nameList), "utf-8");
		List<String> targetList = Files.readAllLines(Paths.get(target), Charset.forName("utf-8"));
		
		for(String t : targetList) {
			try {
				convertor.convert(t);
			} catch (NoSuchMasterNameException e) {
				result.add(t);
			}
		}
		return result;
	}
	
	
	@Test
	public void 테스트() throws Exception {
		
		
		/*String[] names = {"고정철", "박준범", "김민구", "강민성", "김정수", "지용진", "이호창", "박태성", "박태식"};
		for(int i = 0, len=names.length; i<len; i++) {
			save(new Test2(Long.valueOf(i+1), names[i]));
		}*/
		
	}
	
	
	private void 네이티브_프로시저() throws Exception {
		Connection con = session.connection();
		con.setAutoCommit(false);
		CallableStatement cs = con.prepareCall("call test_call(?, ?)");
		cs.setInt(1, 2);
		cs.registerOutParameter("param2", Types.VARCHAR);
		boolean isResult = cs.execute();
		
		if(isResult)
			resultMap(cs.getResultSet());
		while(cs.getMoreResults())
			resultMap(cs.getResultSet());
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT @param2");
		rs.next();
		log(rs.getObject(1));
	}
	
	private void resultMap(ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		int len = meta.getColumnCount();
		System.out.println("\n총 컬럼갯수 : " + len);
		int num = 1;
		while(rs.next()) {
			System.out.print("[" + num++ + "] ");
			for(int i = 0; i < len; i++) {
				System.out.print(rs.getObject(i + 1) + "  ");
			}
			System.out.println("");
			
		}
	}
	
	
	
	public void 거래처등록() throws Exception {
		List<String[]> csv = CSVPull.pull(Paths.get("g:/customer(엑셀).csv"), "euc-kr");
		List<Customer> subList = new ArrayList<>();

		Function<String[], Customer> lambda = CSVCustomer.create(session);

		for (String[] value : csv) {
			subList.add(lambda.apply(value));
		}
		for (Customer sub : subList) {
			session.save(sub);
		}
	}
	
	public void 거래처등록_이름만() throws Exception {
		List<String> csv = Files.readAllLines(Paths.get("g:/customer(only-name).txt"), Charset.forName("utf-8"));
		
		int i = 0;
		Customer customer = null;
		for(String s : csv) {
			
			if(i++ == 0) continue;
			customer = new Customer();
			customer.setName(s);
			session.save(customer);
		}
	}
		
	public void 하청업체등록() throws Exception {
		List<String[]> csv = CSVPull.pull(Paths.get("g:/하청업체.csv"), "euc-kr");
		List<Subcontractor> subList = new ArrayList<>();

		Function<String[], Subcontractor> lambda = CSVSubcontractor.create(session);

		for (String[] value : csv) {
			subList.add(lambda.apply(value));
		}
		for (Subcontractor sub : subList) {
			session.save(sub);
		}
	}
	
	public void 워크리스트_입력() throws Exception {
		
		List<String[]> csv = CSVPull.pull(Paths.get("g:/work_list.csv"), "utf-8", "%");
		List<WorkGroup> subList = new ArrayList<>();
		
		Function<String[], WorkGroup> lambda = CSVWork.create(session);
		
		for(String[] value : csv) {
			subList.add(lambda.apply(value));
		}
		
		for(WorkGroup sub : subList) {
			session.save(sub);
		}
	}
	
	/*
	 * 이름 목록을 검사해서 DB에 등록된 이름을 지워준다.
	 */
	public void 이름목록검사() throws Exception {
		NameConvertor convertor = NameConvert.getConvertorByPath(Paths.get("g:/itemtype_mastername.txt"), "utf-8");
		
		
		// 이름목록 파일, NameConvertor, DB에 저장된 퍼시스턴스 객체, 이름목록을 비교할 객체의 값 getter메서드
		nameCheck(
					"g:/itemtype.csv",
					convertor,
					ItemCategory.class,
					"getName"
				);
		
	}
	
	private<T> void nameCheck(String filePath, NameConvertor convertor, Class<T> targetClass, String getterMethod)
			throws Exception {
		Path target = Paths.get(filePath);
		List<String> targetList = Files.readAllLines(target);
		Set<String> resultSet = new TreeSet<>();
		List<T> types = PersistenceList.get(targetClass);

		String convertName = null;
		Method getter = targetClass.getMethod(getterMethod, new Class<?>[0]);
		
		/*
		 * 컨버팅한 마스터네임과 이름목록을 비교해 일치하지 않는 이름만 골라낸다. 
		 */
		int count = 0;
		
		boolean isMatch = false;
		String getterValue = null;
		
		for(String name : targetList) {
			
			isMatch = false;
			try {
				convertName = convertor.convert(name);
			} catch (NoSuchMasterNameException e) {
				convertName = name;
			}
			
			// DB에 해당 이름이 있는지 확인
			for (T type : types) {
				getterValue = getter.invoke(type).toString();
				if(isMatch = getterValue.equals(convertName))
					break;
			}
			
			if(!isMatch) {
				resultSet.add(convertName);		// 매칭되는게 없으면 새로운 유형이니 처리를 위해 등록한다.
			}
			else count++;							// 매칭되는게 있으면 카운터를 늘린다.
		}
		
//		resultSet.add("((((match count : " + String.valueOf(count));
		
		KoFileUtils.writeLines(resultSet, target);
	}
	
	
	
	
	private SQLQuery sql(String sql) {
		return sql(sql, true);
	}
	private SQLQuery sql(String sql, boolean readOnly) {
		SQLQuery s = session.createSQLQuery(sql);
		s.setReadOnly(readOnly);
		return s;
	}
	
}



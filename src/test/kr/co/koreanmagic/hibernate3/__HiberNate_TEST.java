package kr.co.koreanmagic.hibernate3;

import java.lang.reflect.Method;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.Banner;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Card;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Design;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Envelope;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Goods;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Invitation;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.MasterPrinting;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.PagePrint;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Print;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Sign;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.Sticker;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;


public class __HiberNate_TEST extends HibernateTestDao {

	private static Logger logger = Logger.getLogger(__HiberNate_TEST.class);
	
	private boolean BEFORE = false;

	private static SessionFactory factory;
	private static Session session;
	private static Transaction transaction;
	private static boolean isCurrentSession;
	
	@Before
	public void init() throws Exception {
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
	
	
}



package kr.co.koreanmagic.hibernate.legacy;

import static kr.co.koreanmagic.hibernate.legacy.CSVPull.nullCheck;

import java.nio.file.Paths;
import java.util.Date;
import java.util.function.Function;

import org.hibernate.Session;

import kr.co.koreanmagic.hibernate.legacy.NameConvert.NameConvertor;
import kr.co.koreanmagic.hibernate.mapper.domain.Address;
import kr.co.koreanmagic.hibernate.mapper.domain.BankName;
import kr.co.koreanmagic.hibernate.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Bank;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Email;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Website;
import kr.co.koreanmagic.hibernate.mapper.usertype.ThreeNumber;

public class CSVSubcontractor {
	
	public static<T> Function<String[], T> create(final Session session) { 
		
		final NameConvertor nameConvertor = NameConvert.get(Subcontractor.class);
		
		return (values) -> {
			
			int i = 0;
			
			Subcontractor subcontractor = null;
			String value = null;
			Website website = null;
			Bank bank = null;
			
			subcontractor = new Subcontractor();

			// 1. 사업자번호
			if ((value = nullCheck(values, i++)) != null) {
				subcontractor.setBizNum(new ThreeNumber(value.split("-")));
			}

			// 2. 업체명
			if ((value = nullCheck(values, i++)) != null) {
				subcontractor.setName(value);
			}

			website = new Website();
			// 3. 웹사이트 주소
			if ((value = nullCheck(values, i++)) != null) {
				website.setUrl(value);
			}
			// 4. 웹사이트 아이디
			if ((value = nullCheck(values, i++)) != null) {
				website.setId(value);
			}
			// 5. 웹사이트 비밀번호
			if ((value = nullCheck(values, i++)) != null) {
				website.setPassword(value);
			}
			subcontractor.setWebsite(website);

			
			// 6. 핸드폰
			if ((value = nullCheck(values, i++)) != null)
				subcontractor.setMobile(new ThreeNumber(value.split("-")));
			// 7. 전화번호
			if ((value = nullCheck(values, i++)) != null)
				subcontractor.setTel(new ThreeNumber(value.split("-")));
			// 8. 팩스
			if ((value = nullCheck(values, i++)) != null)
				subcontractor.setFax(new ThreeNumber(value.split("-")));
			// 9. 이메일
			if ((value = nullCheck(values, i++)) != null)
				subcontractor.setEmail(new Email(value.split("@")));

			
			
			// 10. 웹하드 아이디
			if ((value = nullCheck(values, i++)) != null) {
				website = new Website("www.webhard.co.kr");
				website.setId(value);
				subcontractor.setWebhard(website);
			}
			// 11. 웹하드 비밀번호
			if ((value = nullCheck(values, i++)) != null) {
				website.setPassword(value);
			}
			

			
			bank = new Bank();
			// 12. 계좌 주은행
			if ((value = nullCheck(values, i++)) != null) {
				for(BankName bankName : PersistenceList.get(BankName.class)) {
					if(bankName.getName().equals(value)) {
						bank.setBankName(bankName);
						break;
					}
				}
			}
			// 13. 계좌번호
			if ((value = nullCheck(values, i++)) != null)
				bank.setBankNum(value);
			// 14. 계좌주
			if ((value = nullCheck(values, i++)) != null)
				bank.setHolder(value);
			subcontractor.setBank(bank);

			
			// 15. 주소
			if ((value = nullCheck(values, i++)) != null)
				subcontractor.addAddress(new Address(value));

			subcontractor.setInsertTime(new Date());
			
			return (T)subcontractor;
		};
	}

}

package kr.co.koreanmagic.hibernate3.legacy.creator;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import kr.co.koreanmagic.hibernate3.legacy.support.AbstractPersistentCreator;
import kr.co.koreanmagic.hibernate3.legacy.support.PersistenceList;
import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Bank;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Website;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype.CompanyType;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

@Component
public class CreatSubcontractor extends AbstractPersistentCreator<Subcontractor> {

	@Override
	public Subcontractor creat(Map<String, Object> values) {
		
		setMap(values);
		
		Subcontractor subcontractor = null;
		Website website = null;
		Bank bank = null;
		subcontractor = new Subcontractor();

		String value = null;
		

		// 1. 사업자번호
		if (check("bizNum"))
			subcontractor.setBizNum(new ThreeNumber(getValue().split("-")));

		// 2. 업체명
		if (check("name")) {
			subcontractor.setName(getValue());
		}

		website = new Website();
		// 3. 웹사이트 주소
		if (check("website_url")) {
			website.setUrl(getValue());
		}
		// 4. 웹사이트 아이디
		if (check("website_id")) {
			website.setId(getValue());
		}
		// 5. 웹사이트 비밀번호
		if (check("website_password")) {
			website.setPassword(getValue());
		}
		subcontractor.setWebsite(website);

		
		// 6. 핸드폰
		if (check("mobile"))
			subcontractor.setMobile(
						new ThreeNumber(getValue().split("-"))
					);
		// 7. 전화번호
		if (check("tel"))
			subcontractor.setTel(new ThreeNumber(getValue().split("-")));
		// 8. 팩스
		if (check("fax"))
			subcontractor.setFax(new ThreeNumber(getValue().split("-")));
		// 9. 이메일
		if (check("email"))
			subcontractor.setEmail(new Email(getValue().split("@")));

		
		// 10. 웹하드
		if (check("webhard_id")) {
			website = new Website("www.webhard.co.kr");
			website.setId(getValue());
			
			if (check("webhard_password")) {
				website.setPassword(getValue());
			}
			
			subcontractor.setWebhard(website);
		}
		
		
		bank = new Bank();
		// 12. 계좌 주은행
		if (check("bank_name")) {
			bank.setBankName(
						convert(getValue(), BankName.class)
					);
			
			if (check("bank_num"))
				bank.setBankNum(getValue());
			
			if (check("bank_holder"))
				bank.setHolder(getValue());
			
			subcontractor.setBank(bank);
		}
		

		// 15. 주소
		if (check("address"))
			subcontractor.addAddress(new Address(getValue()));

		subcontractor.setInsertTime(Date.from(Instant.now()));
		
		return subcontractor;
	}

}

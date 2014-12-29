package kr.co.koreanmagic.hibernate3.legacy.creator;

import java.util.Map;

import kr.co.koreanmagic.hibernate3.legacy.support.AbstractPersistentCreator;
import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.BizClass;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype.CompanyType;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CreatCustomer extends AbstractPersistentCreator<Customer> {

	
	
	@Override
	public Customer creat(Map<String, Object> values) {
		
		setMap(values);
		
		Customer customer = new Customer();

		String value = null;
		
		// 1. 사업분류
		if (check("companyType"))
			customer.setCompanyType(CompanyType.get(getValue()));

		// 2. 사업자번호
		if (check("bizNum")) {
			customer.setBizNum(new ThreeNumber(getValue().split("-")));
		}

		// 3. 이름
		if (check("name"))
			customer.setName(getValue());

		// 4. 대표자
		if (check("ceoName"))
			customer.setCeoName(getValue());

		// 5. 업태
		if (check("bizClass"))
			customer.setBizClass(
						convert(getValue(), BizClass.class)
					);

		// 6. 종목
		if (check("bizTypes"))
			customer.setBizTypes(getValue());

		// 7. 핸드폰
		if (check("mobile"))
			customer.setMobile(new ThreeNumber(getValue().split("-")));

		// 8. 전화
		if (check("tel"))
			customer.setTel(new ThreeNumber(getValue().split("-")));

		// 9. 팩스
		if (check("fax"))
			customer.setFax(new ThreeNumber(getValue().split("-")));

		// 10. 이메일
		if (check("email"))
			customer.setEmail(new Email(getValue().split("@")));

		// 11. 메모
		if (check("memo"))
			customer.setMemo(getValue());

		// 12. 주소
		if (check("address")) {
			String[] a = getValue().split("★");
			if (a.length == 2)
				customer.addAddress(new Address(a[0], a[1]));
			else
				customer.addAddress(new Address(a[0]));
		}

		
		return customer;
	}

}

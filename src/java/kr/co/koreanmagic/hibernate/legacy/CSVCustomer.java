package kr.co.koreanmagic.hibernate.legacy;

import static kr.co.koreanmagic.hibernate.legacy.CSVPull.nullCheck;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import kr.co.koreanmagic.hibernate.legacy.NameConvert.NameConvertor;
import kr.co.koreanmagic.hibernate.mapper.domain.Address;
import kr.co.koreanmagic.hibernate.mapper.domain.BizClass;
import kr.co.koreanmagic.hibernate.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Email;
import kr.co.koreanmagic.hibernate.mapper.enumtype.CompanyType;
import kr.co.koreanmagic.hibernate.mapper.usertype.ThreeNumber;

import org.hibernate.Session;

/*
 * CSV파일을 읽어들인 후 이를 Customer 영속객체로 만들어준다.
 */
public class CSVCustomer {

	
	public static<T> Function<String[], T> create(final Session session) { 
		
		final List<Customer> types = new ArrayList<>();
		final NameConvertor convertor = NameConvert.get(Customer.class);
		
		return (values) -> {
			
			int i = 0;
			Customer customer = null;
			String value = null;

			customer = new Customer();

			// 1. 사업분류
			if ((value = nullCheck(values, i++)) != null)
				customer.setCompanyType(CompanyType.get(value));

			// 2. 사업자번호
			if ((value = nullCheck(values, i++)) != null) {
				customer.setBizNum(new ThreeNumber(value.split("-")));
			}

			// 3. 이름
			if ((value = nullCheck(values, i++)) != null)
				customer.setName(value);

			// 4. 대표자
			if ((value = nullCheck(values, i++)) != null)
				customer.setCeoName(value);

			// 5. 업태
			if ((value = nullCheck(values, i++)) != null)
				customer.setBizClass(BizClass.find(value));

			// 6. 종목
			if ((value = nullCheck(values, i++)) != null)
				customer.setBizTypes(value);

			// 7. 핸드폰
			if ((value = nullCheck(values, i++)) != null)
				customer.setMobile(new ThreeNumber(value.split("-")));

			// 8. 전화
			if ((value = nullCheck(values, i++)) != null)
				customer.setTel(new ThreeNumber(value.split("-")));

			// 9. 팩스
			if ((value = nullCheck(values, i++)) != null)
				customer.setFax(new ThreeNumber(value.split("-")));

			// 10. 이메일
			if ((value = nullCheck(values, i++)) != null)
				customer.setEmail(new Email(value.split("@")));

			// 11. 홈페이지
			if ((value = nullCheck(values, i++)) != null)
				customer.setMemo(value);

			// 12. 주소
			if ((value = nullCheck(values, i++)) != null) {
				String[] a = value.split("★");
				if (a.length == 2)
					customer.addAddress(new Address(a[0], a[1]));
				else
					customer.addAddress(new Address(value));
			}

			// 13. 메모
			if ((value = nullCheck(values, i++)) != null)
				customer.setMemo(value);

			types.add(customer);
			i = 0;

			return (T) customer;
		};
	}
}

package kr.co.koreanmagic.hibernate.legacy;

import static kr.co.koreanmagic.hibernate.legacy.CSVPull.nullCheck;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.Function;

import kr.co.koreanmagic.hibernate.legacy.NameConvert.NameConvertor;
import kr.co.koreanmagic.hibernate.mapper.category.ItemCategory;
import kr.co.koreanmagic.hibernate.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate.mapper.domain.Work;
import kr.co.koreanmagic.hibernate.mapper.domain.WorkGroup;
import kr.co.koreanmagic.hibernate.mapper.domain.WorkState;
import kr.co.koreanmagic.hibernate.mapper.embeddable.Size;

import org.hibernate.Session;
import org.joda.time.DateTime;

/*
 * CSV파일을 읽어들인 후 이를 Work 영속객체로 만들어준다.
 */
public class CSVWork {

	public static<T> Function<String[], T> create(final Session session) { 
		
		final NameConvertor customerMasterNames = NameConvert.get(Customer.class);
		final NameConvertor itemMasterNames = NameConvert.get(ItemCategory.class);
		final NameConvertor subcontractorMasterNames = NameConvert.get(Subcontractor.class);
		
		return (values) -> {
			
			WorkGroup workGroup = null;
			Work work = null;
			String value = null;
			
			String customerName = null; // 레거시 데이터의 업체명
			
			int i = 0;
			
			workGroup = new WorkGroup();
			work = new Work();
			
			workGroup.addWork(work);
			
			try {
				
				// 1. 식별자
				if ((value = nullCheck(values, i++)) != null) {
					workGroup.setId(value);
				}
				
			// 2. 입력시간
			if ((value = nullCheck(values, i++)) != null) {
				value = value.replaceAll(" ", "T");
				Date insertTime = new DateTime(value).toDate();
				workGroup.setInsertTime(insertTime);
				work.setInsertTime(insertTime);
			}

			// 3. 작업상황
			if ((value = nullCheck(values, i++)) != null) {
				// ID값이 동일하므로 바로 만들어버린다.
				WorkState state = new WorkState(Long.valueOf(value));
				workGroup.setState(state);
			}

			// 4. 거래처명
			if ((value = nullCheck(values, i++)) != null) {
				value = customerMasterNames.convert(value);
				for(Customer customer : PersistenceList.get(Customer.class)) {
					if(customer.getName().equals(value)) {
						workGroup.setCustomer(customer);
						break;
					}
				}
			}

			
			// 5. 품목
			if ((value = nullCheck(values, i++)) != null) {
				value = itemMasterNames.convert(value);
				for(ItemCategory category : PersistenceList.get(ItemCategory.class)) {
					if(category.getName().equals(value)) {
						work.setItemCategory(category);
						break;
					}
				}
			}

			// 6. 품목상세
			if ((value = nullCheck(values, i++)) != null) {
				work.setItemMemo(value);
			}

			// 7. 건수
			if ((value = nullCheck(values, i++)) != null) {
				work.setCount(Integer.valueOf(value));
			}

			// 8. 수량
			if ((value = nullCheck(values, i++)) != null) {
				work.setNumber(value);
			}

			// 9. 사이즈
			/*
			 * 사이즈 없이 "known"이라고 된 컬럼이 있다.
			 */
			if ((value = nullCheck(values, i++)) != null) {
				if(!value.equals("known") || value.contains("-")) {
					String[] sizes = value.split("-");
					for(int j = 0; j < 2; j++) {
						int pos = sizes[j].indexOf(".");
						if(pos != -1) {
							sizes[j] = sizes[j].substring(0, pos);
						}
					}
					Size size = new Size();
					work.setSize(size);
				}
			}

			// 10. 재단사이즈
			i++;

			// 11. 단위
			if ((value = nullCheck(values, i++)) != null) {
				work.setUnit(value);
			}

			// 12. 메모 (나중에 DB에서 직접 쿼리를 날려 입력하자. 줄바꿈 때문에 CSV에서 에러가 난다.
			i++;
			/*if ((value = nullCheck(values, i++)) != null) {
				work.setMemo(customerName + " / " + value);
			}*/

			// 13. 게시물제목
			if ((value = nullCheck(values, i++)) != null) {
				workGroup.setSubject(value);
			}
			// 제목이 없는 레코드가 있다.
			else {
				workGroup.setSubject("제목없음");
			}

			// 14. 제작단가
			if ((value = nullCheck(values, i++)) != null) {
				work.setCost(Integer.valueOf(value));
			}

			// 15. 공급단가
			if ((value = nullCheck(values, i++)) != null) {
				work.setCost(Integer.valueOf(value));
			}

			// 16. 업데이트 시간
			if ((value = nullCheck(values, i++)) != null) {
				value = value.replaceAll(" ", "T");
				work.setUpdateTime(new Timestamp(new DateTime(value)
						.getMillis()));
			}

			// 17. 배송 방법
			i++;

			// 18. 배송 시간
			if ((value = nullCheck(values, i++)) != null) {
				value = value.replaceAll(" ", "T");
				work.setDeliveryDate(new DateTime(value).toDate());
			}

			// 19. 하청업체
			if ((value = nullCheck(values, i++)) != null) {
				value = subcontractorMasterNames.convert(value);
				for(Subcontractor subconstractor : PersistenceList.get(Subcontractor.class)) {
					if(subconstractor.getName().equals(value)) {
						work.setSubconstractor(subconstractor);
						break;
					}
				}
			}

			// 20. 조회수
			if ((value = nullCheck(values, i++)) != null) {
				workGroup.setReadCount(Integer.valueOf(value));
			}

			// 21. 재단사이즈
			if ((value = nullCheck(values, i++)) != null) {
				String[] bleedSize = null;
				if(!value.equals("known") || value.contains("-")) {
					bleedSize = value.split("-");
					if(bleedSize.length == 2) {
						Size size = new Size();
						work.setBleedSize(size);
					}
				}
			}

			// 22. 배송메모 (나중에 DB에서 직접 쿼리를 날려 입력하자. 줄바꿈 때문에 CSV에서 에러가 난다.
			i++;

			// 23. 읽음 확인
			i++;

			return (T)workGroup;
			
			} catch (Exception e) {
				throw new RuntimeException("식별자: " + workGroup.getId() + "  ", e);
			}
			
		};
	}
}

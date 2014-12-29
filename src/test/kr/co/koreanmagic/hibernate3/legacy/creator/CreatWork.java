package kr.co.koreanmagic.hibernate3.legacy.creator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import kr.co.koreanmagic.hibernate3.legacy.support.AbstractPersistentCreator;
import kr.co.koreanmagic.hibernate3.legacy.support.NameConvertManager;
import kr.co.koreanmagic.hibernate3.legacy.support.NameConvertor;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.hibernate3.mapper.domain.Work;
import kr.co.koreanmagic.hibernate3.mapper.domain.WorkGroup;
import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.WorkState;
import kr.co.koreanmagic.hibernate3.mapper.domain.support.embeddable.Size;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class CreatWork extends AbstractPersistentCreator<WorkGroup> {
	
	private NameConvertor customerMasterNames = NameConvertManager.get(Customer.class);
	private NameConvertor itemMasterNames = NameConvertManager.get(ItemCategory.class);
	private NameConvertor subcontractorMasterNames = NameConvertManager.get(Subcontractor.class);

	static final String[] SELECT_COLUMNS = {
		"id", "insert_time", "con_id", "customer", "item", "item_type", "count", "number", "size", "bleed",
		"unit", "tag", "cost", "price", "update_time", "delivery", "delivery_time", "subcontract", "read_count",
		"bleed_size", "read_check"
	};
	
	@Override
	public WorkGroup creat(Map<String, Object> values) {
		
		setMap(values);
		
		WorkGroup workGroup = null;
		Work work = null;
		String value = null;
		
		
		workGroup = new WorkGroup();
		work = new Work();
		
		workGroup.addWork(work);
		
		try {
			
			// 1. 식별자
			if (check("id")) {
				workGroup.setId(getValue());
			}
			
		// 2. 입력시간
		if (check("insert_time")) {
			value = getValue().replaceAll(" ", "T");
			Date insertTime = new DateTime(value).toDate();
			workGroup.setInsertTime(insertTime);
			work.setInsertTime(insertTime);
		}

		// 3. 작업상황
		if (check("con_id")) {
			workGroup.setState(
								convert(Integer.valueOf(getValue()), WorkState.class)
							);
		}

		// 4. 거래처명
		/*if (check("customer")) {
			work.setTag(getValue()); // 레거시 데이터의 거래처명은 혹시 모르니까 Tag로 기입한다.
			value = customerMasterNames.convert(getValue());	// 이름 컨버팅
			workGroup.setCustomer(
						convert(value, Customer.class)
					);
		}*/

		
		// 5. 품목
		if (check("item")) {
			value = itemMasterNames.convert(getValue());
			work.setItemCategory(
						convert(value, ItemCategory.class)
					);
		}

		// 6. 품목상세
		if (check("item_type")) {
			work.setSubject(getValue());
		}

		// 7. 건수
		if (check("count")) {
			work.setCount(Integer.valueOf(getValue()));
		}

		// 8. 수량
		if (check("number")) {
			work.setNumber(getValue());
		}

		// 9. 사이즈
		/*
		 * 사이즈 없이 "known"이라고 된 컬럼이 있다.
		 */
		if (check("size")) {
			value = getValue();
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

		// 11. 단위
		if (check("unit")) {
			work.setUnit(getValue());
		}

		// 12. 메모 (나중에 DB에서 직접 쿼리를 날려 입력하자. 줄바꿈 때문에 CSV에서 에러가 난다.
		/*if ((value = nullCheck(values, i++)) != null) {
			work.setMemo(customerName + " / " + value);
		}*/

		
		// 13. 게시물제목
		if (check("tag")) {
			workGroup.setSubject(getValue());
		}
		// 제목이 없는 레코드가 있다.
		else {
			workGroup.setSubject("제목없음");
		}

		
		// 14. 제작단가
		if (check("cost")) {
			work.setCost(Integer.valueOf(getValue()));
		}

		// 15. 공급단가
		if (check("price")) {
			work.setCost(Integer.valueOf(getValue()));
		}

		// 16. 업데이트 시간
		if (check("update_time")) {
			value = getValue().replaceAll(" ", "T");
			work.setUpdateTime(new Timestamp(new DateTime(value)
					.getMillis()));
		}

		// 17. 배송 방법

		// 18. 배송 시간
		if (check("delivery_time")) {
			value = getValue().replaceAll(" ", "T");
			work.setDeliveryDate(new DateTime(value).toDate());
		}

		// 19. 하청업체
		if (check("subcontract")) {
			value = subcontractorMasterNames.convert(getValue());
			work.setSubconstractor(
						convert(value, Subcontractor.class)
					);
		}

		// 20. 조회수
		if (check("read_count")) {
			workGroup.setReadCount(Integer.valueOf(getValue()));
		}

		// 21. 재단사이즈
		if (check("bleed_size")) {
			String[] bleedSize = null;
			value = getValue();
			if(!value.equals("known") || value.contains("-")) {
				bleedSize = value.split("-");
				if(bleedSize.length == 2) {
					Size size = new Size();
					work.setBleedSize(size);
				}
			}
		}

		return workGroup;
		
		} catch (Exception e) {
			throw new RuntimeException("식별자: " + workGroup.getId() + "  ", e);
		}
	}
	
}

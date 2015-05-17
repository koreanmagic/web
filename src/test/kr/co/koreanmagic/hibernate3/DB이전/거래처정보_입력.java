package kr.co.koreanmagic.hibernate3.DB이전;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class 거래처정보_입력 extends TestManager {

	
	@Test
	//@Transactional
	//@Rollback(false)
	public void test() throws Exception {
		
		for(Value v : 주소()) {
			customer = cs.get(v.getId());
			customer.addAddress(v.getValue());
			cs.update(customer);
			cs.flush();
			
			log(v);
		}
		
	}
	
	@Transactional
	@Rollback(false)
	private List<Value> 주소() throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					if( line.length() < 2 ) return null;
					Customer customer = cs.get(id);
					Address r = new Address();
					r.setText(line);
					r.setName( customer.getCeoName() );
					r.setMobile( customer.getMobile() );
					
					return r;
					
				};
		return getData(lamda, "customer_address.txt");
	}
	
	
	private List<Value> 그냥글씨(String file) throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					return line;
				};
		return getData(lamda, file);
	}

	private List<Value> 번호(String file) throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					return new ThreeNumber(line.split("-"));
				};
		return getData(lamda, file);
	}
	
	private List<Value> 이메일() throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					return new Email(line.split("@"));
				};
		return getData(lamda, "customer_email.txt");
	}
	
	
	

}

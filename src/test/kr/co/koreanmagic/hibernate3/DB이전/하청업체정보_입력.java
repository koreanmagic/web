package kr.co.koreanmagic.hibernate3.DB이전;

import java.util.List;
import java.util.function.BiFunction;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.code.BankName;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Email;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Webhard;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Website;
import kr.co.koreanmagic.hibernate3.mapper.usertype.ThreeNumber;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class 하청업체정보_입력 extends TestManager {

	
	@Test
	@Transactional
	@Rollback(false)
	public void test() throws Exception {
		
		int i = 1;
		for(Value v : 번호("sub_bizNum.txt")) {
			sub = ss.get(v.getId());
			sub.setBizNum(v.getValue());
			ss.update(sub);
			ss.flush();
			
			log(i++ + ") " + v);
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
	
	private List<Value> 웹하드() throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					String[] d = line.split("\\$");
					Webhard h = new Webhard();
					h.setUserId(d[0]);
					h.setPassword(d[1]);
					return h;
				};
		return getData(lamda, "sub_webhard.txt");
	}
	
	private List<Value> 홈페이지() throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					if(!line.contains("$")) return null;
					Website web = new Website();
					String[] d = line.split("\\$");
					web.setUrl(d[0]);
					web.setId(d[1]);
					web.setPassword(d[2]);
					return web;
				};
		return getData(lamda, "sub_web.txt");
	}
	
	
	private List<Value> 은행() throws Exception {
		BiFunction<Long, String, Object> lamda =
				(id, line) -> {
					String[] d = line.split("\\$");
					BankName b = bns.findByName(d[0]);
					Bank bank = new Bank();
					bank.setBankName(b);
					bank.setAccountNum(d[1]);
					bank.setHolder(d[2]);
					return bank;
				};
		return getData(lamda, "sub_bank.txt");
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
		return getData(lamda, "sub_email.txt");
	}
	

}

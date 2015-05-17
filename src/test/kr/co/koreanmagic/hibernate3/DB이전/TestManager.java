package kr.co.koreanmagic.hibernate3.DB이전;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.hibernate3.스프링_하이버네이트_테스트;
import kr.co.koreanmagic.hibernate3.DB이전.support.NameConvertManager;
import kr.co.koreanmagic.hibernate3.DB이전.support.NameConvertor;
import kr.co.koreanmagic.hibernate3.config.EnableHibernate3;
import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;
import kr.co.koreanmagic.service.AddressService;
import kr.co.koreanmagic.service.BankNameService;
import kr.co.koreanmagic.service.BankService;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.service.ManagerService;
import kr.co.koreanmagic.service.SubcontractorService;
import kr.co.koreanmagic.service.WorkService;
import kr.co.koreanmagic.service.WorkStateService;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Configuration
@EnableAspectJAutoProxy
@EnableHibernate3
@ComponentScan({
	"kr.co.koreanmagic.dao",
	"kr.co.koreanmagic.service",
})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={스프링_하이버네이트_테스트.class})
public class TestManager {
	
	NameConvertor customerName = NameConvertManager.get("customer");
	NameConvertor subcontractorName = NameConvertManager.get("subcontractor");
	NameConvertor itemName = NameConvertManager.get("item");
	@Autowired CustomerService cs;
	@Autowired SubcontractorService ss;
	@Autowired ManagerService ms;
	@Autowired WorkService ws;
	@Autowired AddressService as;
	@Autowired BankService bs;
	@Autowired BankNameService bns;
	@Autowired WorkStateService stateService;
	
	Customer customer = new Customer();
	Subcontractor sub = new Subcontractor();
	Manager manager = new Manager();
	Address address = new Address();
	Bank bank = new Bank();
	
	void log(Object...obj) {
		System.out.println( KoStringUtils.join(", ", obj) );
	}
	
	
	//정보입력을 위한 유틸
	/*
	 * [id,데이터]
	 * 로 넣어주면 넣게 좋게 정렬해준다.
	 */
	List<Value> getData(BiFunction<Long, String, Object> lamda, String file) throws Exception {
		List<String> datas = Files.readAllLines( Paths.get("g:/" + file), Charset.forName("utf-8") );
		int i = 1, l = datas.size();
		
		List<Value> result = new ArrayList<>();
		String[] split = null;
		Object value = null;
		
		for(;i<l;i++) {
			split = datas.get(i).split(",");
			if(split.length == 2 && split[1].length() > 0) {
				value = lamda.apply(Long.valueOf(split[0]), split[1]);
				if(value != null) result.add( new Value(split[0],  value));
			}
		}
		
		return result;
	}
	
	static class Value {
		private Long id;
		private Object value;
		
		public Value(Object id, Object value) {
			this.id = Long.valueOf(id.toString());
			this.value = value;
		}
		public Long getId() {
			return id;
		}
		public<T> T getValue() {
			return (T)value;
		}
		
		@Override
		public String toString() {
			return getId() + " / " + getValue();
		}
	}

}

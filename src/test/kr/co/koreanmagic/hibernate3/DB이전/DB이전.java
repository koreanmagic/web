package kr.co.koreanmagic.hibernate3.DB이전;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.hibernate3.스프링_하이버네이트_테스트;
import kr.co.koreanmagic.hibernate3.DB이전.support.NameConvertManager;
import kr.co.koreanmagic.hibernate3.DB이전.support.NameConvertor;
import kr.co.koreanmagic.hibernate3.DB이전.support.NoSuchMasterNameException;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


public class DB이전 extends TestManager {

	NameConvertor customerName = NameConvertManager.get("customer");
	NameConvertor subcontractorName = NameConvertManager.get("subcontractor");
	@Autowired CustomerService cs;
	@Autowired SubcontractorService ss;
	@Autowired ManagerService ms;
	@Autowired WorkService ws;
	@Autowired AddressService as;
	@Autowired BankService bs;
	@Autowired BankNameService bns;
	
	Customer customer = new Customer();
	Subcontractor sub = new Subcontractor();
	Manager manager = new Manager();
	Address address = new Address();
	Bank bank = new Bank();
	
	@Test
	@Transactional
	@Rollback(false)
	public void 거래처이름() throws Exception {
		getNameSet(customerName, "customer.txt");
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void 하청업체이름() throws Exception {
		TreeSet<String> set = getNameSet(subcontractorName, "subcontract.txt");
		int i = 1;
		for(String s : set) {
			sub = new Subcontractor();
			sub.setName(s);
			ss.add(sub);
		}
		
	}
	
	public TreeSet<String> getNameSet(NameConvertor c, String fileName) throws Exception {
		TreeSet<String> set = new TreeSet<>();
		List<String> file = Files.readAllLines( Paths.get("g:/" + fileName), Charset.forName("utf-8") );
		int i = 1;
		
		for(String f : file) {
			try {
				log(c.convert(f));
			} catch(NoSuchMasterNameException e) {
				log("에러:::: " + e.getName());
			}
		}
		return set;
	}
	
	private List<String> read(String file) throws Exception {
		return Files.readAllLines( Paths.get("g:/", file), Charset.forName("utf-8") );
	}

	
}

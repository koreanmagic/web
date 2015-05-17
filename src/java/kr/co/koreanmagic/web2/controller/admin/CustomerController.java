package kr.co.koreanmagic.web2.controller.admin;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.service.BankNameService;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.service.ManagerService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/customer")
@SessionAttributes("command")
public class CustomerController extends PartnerController<Customer> {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired private CustomerService service;
	
	@Autowired private BankNameService bankNameService;
	@Autowired private ManagerService managerService;

	@Override
	public CustomerService getService() {
		return this.service;
	}
	@Override
	public String name() { return "customer"; }	
	
	// ************************  [Ajax] 거래처 이름검색  ************************ //
	@RequestMapping(value="/search/name/{key}")
	@ResponseBody
	public Object ajaxCustomer(@PathVariable("key") String key) throws Exception {
		return service.likeByName("%"+key+"%");
	}
	
	
}

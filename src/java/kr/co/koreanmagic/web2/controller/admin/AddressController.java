package kr.co.koreanmagic.web2.controller.admin;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.service.AddressService;
import kr.co.koreanmagic.web2.controller.admin.PartnerController.PartnerMemberController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/address")
@SessionAttributes({"command"})
public class AddressController extends PartnerMemberController<Address> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired AddressService service;

	@Override
	public AddressService getService() {
		return this.service;
	}
	@Override
	public String name() { return "address"; }

}

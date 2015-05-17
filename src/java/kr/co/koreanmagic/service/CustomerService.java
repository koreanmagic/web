package kr.co.koreanmagic.service;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerService extends PartnerService<Customer> {

	
	
	@Transactional(readOnly=true)
	public List<Customer> likeByName(String like) {
		return getDao().findByCriteria(false, Restrictions.like("name", like));
	}
	
	@Override
	public Customer getInitalBean() {
		Customer customer = new Customer();
		return customer;
	}

	@Override
	public Customer getDefaultBean() {
		return null;
	}

}

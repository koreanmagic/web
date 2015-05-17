package kr.co.koreanmagic.service;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public abstract class PartnerService<T extends Partner> extends GenericService<T, Long> {
	
	@Autowired private BankNameService bankNamesService;
	@Autowired private BankService bankService;
	@Autowired private AddressService addressService;
	@Autowired private WorkService workService;
	@Autowired private ManagerService managerService;
	
	
	//조회에 사용할 모든 연관관계 로딩
	// Partner의 모든 정보를 출력한 view에 사용한다.
	@Transactional(readOnly=true)
	public Partner getInitialValues(Long partnerId) {
		Partner partner = (Partner)getDao().criteria()
								.add(Restrictions.idEq(partnerId))
								.uniqueResult();
		
		Hibernate.initialize(partner.getAddress());
		Hibernate.initialize(partner.getManager());
		Hibernate.initialize(partner.getBanks());
		
		return partner;
	}
	
	
	public T findByName(String name) {
		return getDao().eq(true, "name", name);
	}
	
	
	@Transactional
	public T merge( Long id, T bean ) {
		T stale = get(id);
		return (T)getDao().getSession().merge(bean);
	}
	
	// 매니저 추가
	@Transactional
	public T addManager(Long partnerId, Manager manager) {
		Partner partner = get(partnerId);
		partner.addManager(manager);
		return (T)partner;
	}
	// 주소 추가
	@Transactional
	public T addAddress(Long partnerId, Address address) {
		Partner partner = get(partnerId);
		partner.addAddress(address);
		return (T)partner;
	}
	// 은행 추가
	@Transactional
	public T addBank(Long partnerId, Bank bank) {
		Partner partner = get(partnerId);
		partner.addBank( bank);
		return (T)partner;
	}
	
	
}

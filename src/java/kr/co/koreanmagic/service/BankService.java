package kr.co.koreanmagic.service;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Bank;
import kr.co.koreanmagic.service.marker.PartnerMemberService;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BankService extends GenericService<Bank, Long> implements PartnerMemberService<Bank>  {

	@Override
	public Bank getInitalBean() {
		return new Bank();
	}

	@Override
	public Bank getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getList(Long partnerId) {
		return getDao().criteria().add(Restrictions.eq("partner.id", partnerId)).list();
	}

}

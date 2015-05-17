package kr.co.koreanmagic.service;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Address;
import kr.co.koreanmagic.service.marker.PartnerMemberService;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddressService  extends GenericService<Address, Long> implements PartnerMemberService<Address> {

	@Override
	public Address getInitalBean() {
		return new Address();
	}

	@Override
	public Address getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	@Override
	public List<Address> getList(Long partnerId) {
		return getDao().criteria().add(Restrictions.eq("partner.id", partnerId)).list();
	}
}

package kr.co.koreanmagic.service;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Manager;
import kr.co.koreanmagic.service.marker.PartnerMemberService;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManagerService  extends GenericService<Manager, Long> implements PartnerMemberService<Manager> {
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Manager getInitalBean() {
		// view단에서 null 방지를 위한 대책 ㅜㅜ
		Manager manager = new Manager();
		return manager;
	}

	@Override
	public Manager getDefaultBean() {
		throw new UnsupportedOperationException();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Manager> getList(Long partnerId) {
		return getDao().criteria().add(Restrictions.eq("partner.id", partnerId)).list();
	}
	

}

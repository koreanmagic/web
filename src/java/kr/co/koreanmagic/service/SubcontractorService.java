package kr.co.koreanmagic.service;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Subcontractor;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SubcontractorService extends PartnerService<Subcontractor> {

	@Override
	public Subcontractor getInitalBean() {
		Subcontractor bean = new Subcontractor();
		return bean;
	}

	@Override
	public Subcontractor getDefaultBean() {
		throw new UnsupportedOperationException();
	}

	public Subcontractor findByName(String name) {
		return getDao().eq(true, "name", name);
	}
	
	/*@Transactional(readOnly=true)
	public List<Map<String, String>> getValueMap(String name) {
		return getValueListMap( likeByName(name) , new String[]{ "id", "name", "tel" }, null);
	}*/
	
	@Transactional(readOnly=true)
	public List<Subcontractor> likeByName(String like) {
		return getDao().findByCriteria(false, Restrictions.like("name", like, MatchMode.ANYWHERE));
	}
	
}

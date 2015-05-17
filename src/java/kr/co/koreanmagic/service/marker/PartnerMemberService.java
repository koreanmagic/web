package kr.co.koreanmagic.service.marker;

import java.util.List;

import kr.co.koreanmagic.web.service.Service;

public interface PartnerMemberService<T> extends Service<T, Long>{

	public List<T> getList(Long partnerId);
}

package kr.co.koreanmagic.web2.controller.admin;

import javax.servlet.http.HttpSession;

import kr.co.koreanmagic.hibernate3.mapper.domain.Partner;
import kr.co.koreanmagic.hibernate3.mapper.domain.marker.PartnerMember;
import kr.co.koreanmagic.service.BankNameService;
import kr.co.koreanmagic.service.PartnerService;
import kr.co.koreanmagic.service.marker.PartnerMemberService;
import kr.co.koreanmagic.web.support.board.GeneralBoard;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Board;
import kr.co.koreanmagic.web2.support.argresolver.annotation.Service;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/*
 * Customer와 Subcontractor의 공통부분만 설정한다.
 */
@SessionAttributes({"command"})
public abstract class PartnerController<T extends Partner> extends AdminController<T, Long> {

	private Logger logger = Logger.getLogger(getClass());

	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Insert  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="insert", method=RequestMethod.GET)
	public String insertGet( ModelMap model ) throws Exception {
		
		model.put("command", getService().getInitalBean());
		return "admin.partner.insert";
	}
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String insertPost(ModelMap model,
								@ModelAttribute(value="command") T partner,
								SessionStatus sessionStatus
							) throws Exception {
		
		getService().add(partner);
		sessionStatus.isComplete();
		return "redirect:/admin/" + name() + "/view/" + partner.getId();
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  View  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/view/{id}", method=RequestMethod.GET)
	public String view(ModelMap model,
						@PathVariable("id") Long customerId,
						@Service BankNameService bankNameService,
						HttpSession session
						) throws Exception {
		
		session.setAttribute("partner", customerId);
		model.put("bankNames", bankNameService.getAll());
		
		return "admin.partner.view";
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  List  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(@Board GeneralBoard boardList ) throws Exception {
		boardList.setRowCount( (int)getService().rowCount() );
		boardList.setList( getService().getList(boardList) );
		return "admin.partner.list";
	}
	
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Ajax  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //
	//거래처, 하청업체 이름 검색(like)
	@RequestMapping(value="/search/byname/{word}", method=RequestMethod.GET)
	@ResponseBody
	public Object getNames(
							@PathVariable("word") String word,
							@RequestParam(value="props", required=false) String[] props
							) throws Exception {
		PartnerService<?> service = (PartnerService<?>)getService();
		return props == null ? service.getNameList(word) : service.getNameList(word, props);
	}
	
	
	public static abstract class PartnerMemberController<T extends PartnerMember> extends AdminController<T, Long> {
		
		@RequestMapping(value="/ajax/list/{partnerId}", method=RequestMethod.GET)
		@ResponseBody
		public Object list( @PathVariable("partnerId") Long id ) {
			return ((PartnerMemberService<?>)getService()).getList(id);
		}
		
		@RequestMapping(value="/ajax/add/{partnerId}", method=RequestMethod.POST)
		@ResponseBody
		public void add( @ModelAttribute T bean, @PathVariable("partnerId") Partner partner ) {
			System.out.println(partner.getId());
			bean.setPartner(partner);
			getService().add(bean);
		}		
	}



	/*@Override
	protected GenericService<T, Long> getService() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String name() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
}

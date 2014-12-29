package kr.co.koreanmagic.web2.support;

import java.util.List;

import kr.co.koreanmagic.hibernate3.mapper.domain.Customer;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.web.support.board.GeneralBoardList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CustomerBoardList extends GeneralBoardList<Customer> {

	@Autowired CustomerService service;
	
	@Override
	protected void init() {
		System.out.println("씐난다");
	}

	@Override
	protected List<Customer> loadList(int start, int limit) {
		return service.getList(start, limit);
	}

	@Override
	protected long rowCount() {
		return service.rowCount();
	}

}

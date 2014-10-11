package kr.co.koreanmagic.web.support;

import kr.co.koreanmagic.web.controller.support.GenericBoardList;
import kr.co.koreanmagic.web.domain.Work;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class WorkBoardList extends GenericBoardList<Work> {
	
	public WorkBoardList() {
		super(Work.class);
	}
	
}

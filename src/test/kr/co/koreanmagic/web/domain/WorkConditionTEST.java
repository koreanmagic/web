package kr.co.koreanmagic.web.domain;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class WorkConditionTEST {

	@Test
	public void compare_1() {
		WorkCondition condition1 = getCondition(1);
		WorkCondition condition2 = getCondition(2);
		
		assertThat(condition1.compareTo(condition2), is(-1));
	}
	@Test
	public void compare_2() {
		WorkCondition condition1 = getCondition(1);
		WorkCondition condition2 = getCondition(1);
		
		assertThat(condition1.compareTo(condition2), is(0));
	}
	@Test
	public void compare_3() {
		WorkCondition condition1 = getCondition(2);
		WorkCondition condition2 = getCondition(1);
		
		assertThat(condition1.compareTo(condition2), is(1));
	}
	
	
	
	private WorkCondition getCondition(int id) {
		return getCondition(id, null);
	}
	private WorkCondition getCondition(int id, String name) {
		WorkCondition condition = new WorkCondition();
		condition.setId(id);
		condition.setName(name);
		return condition;
	}

}

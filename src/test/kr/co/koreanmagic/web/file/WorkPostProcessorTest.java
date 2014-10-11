package kr.co.koreanmagic.web.file;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import kr.co.koreanmagic.web.domain.Work;

import org.junit.Test;

public class WorkPostProcessorTest {

	@Test
	public void 사이즈로_아이템() {
		Work work = new Work();
		work.setSize("92-52");
		
		assertThat(WorkPostProcessor.decideItem(work).getItem(), is("명함"));
	}

}

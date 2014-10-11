package kr.co.koreanmagic.web.file;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ItemParserTest {

	@Test
	public void test() {
		assertThat(ItemParser.searchItem("90-58"), is("명함"));
	}

}

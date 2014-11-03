package kr.co.koreanmagic.web.support.nav;

import java.nio.file.Paths;

import kr.co.koreanmagic.web.support.page.PageContextImpl;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PageContextTest {

	@Test
	public void test() {
		test("name///nn///df///", "name/nn/df");
	}
	
	private void test(String path, String result) {
		assertThat(new PageContextImpl(path).getPath(), is(result));
	}

}

package kr.co.koreanmagic.web.file;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class WorkFactoryTest {

	Logger logger = Logger.getLogger("test");
	
	private final StringBuilder buf = new StringBuilder();
	private final String EXAMPLE = "120123_마평텔레콤(김천)_[2]_262-187mm(2).pdf";
	private String id = "120123001",
					customer = "마평텔레콤",
					item = "전단",
					size = "262-187",
					unit = "mm",
					fileType = "pdf",
					date = "2012-01-23",
					tag = "김첨지";
	private int bleed = 2, number = 0, count = 2;
	
	@Test
	public void 매칭임시테스트() {
		//regexView(getMatcher("(\\d+)", "1"));
	}
	
	@Test
	public void 아이디() {
		String id = WorkFactory.selectId(EXAMPLE);
		assertThat(id, is(this.id));
	}
	
	@Test
	public void 사이즈() {
		String size = WorkFactory.selectSize(EXAMPLE);
		assertThat(size, is(this.size));
	}

	@Test
	public void null테스트() {
		String size = WorkFactory.selectSize(EXAMPLE.replace("262-187", ""));
		assertThat(size, nullValue());
	}
	
	@Test
	public void 날짜테스트() {
		Date date = WorkFactory.selectDate(EXAMPLE);
		assertThat(DateFormatUtils.format(date, "yyyy-MM-dd"), is(this.date));
	}
	
	@Test
	public void 거래처명테스트() {
		String customer = WorkFactory.selectCustomer(EXAMPLE);
		assertThat(customer, is(this.customer));
	}
	
	@Test
	public void 단위테스트() {
		String unit = WorkFactory.selectUnit(EXAMPLE);
		assertThat(unit, is(this.unit));
	}
	
	@Test
	public void 재단선테스트() {
		/* 없으면 0이 반환된다 */
		int bleed = WorkFactory.selectBleed(EXAMPLE);
		assertThat(bleed, is(this.bleed));
	}
	
	@Test
	public void 수량테스트() {
		/* 없으면 0이 반환된다 */
		int number = WorkFactory.selectNumber(EXAMPLE);
		assertThat(number, is(this.number));
	}
	
	@Test
	public void 건수테스트() {
		/* 없으면 0이 반환된다 */
		int count = WorkFactory.selectCount(EXAMPLE);
		assertThat(count, is(this.count));
	}
	
	@Test
	public void 파일확장자() {
		/* 없으면 0이 반환된다 */
		String fileType = WorkFactory.selectFileType(EXAMPLE);
		assertThat(fileType, is(this.fileType));
	}
	
	
	@Test  // ★★★★★★★★
	public void 아이템테스트() {
		String item = WorkFactory.selectItem(EXAMPLE);
		assertThat(item, is(this.item));
	}
	
	//@Test  // ★★★★★★★★
	public void 태그테스트2() {
		String tag = WorkFactory.selectTag(EXAMPLE);
		assertThat(tag, is(this.tag));
	}
	
	
	private Matcher getMatcher(String regex, String s) {
		return Pattern.compile(regex).matcher(s);
	}
	private void regexView(Matcher m) {
		String result = "";
		boolean flag = false;
		while(m.find()) {
			flag = true;
			logger.debug(m.group());
			if(m.groupCount() > 0) {
				for(int i = 0; i < m.groupCount(); i++) {
					result += "【" + (i + 1) + "】 " + m.group(i+1) + ", ";  
				}
				logger.debug(result);
				result = "";
			}
		}
		if(!flag) logger.debug("매칭되는게 없습니다.");
	}
	

}

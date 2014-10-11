package kr.co.koreanmagic.web.file;


import kr.co.koreanmagic.web.domain.Work;

import org.apache.log4j.Logger;

public class WorkPostProcessor {
	
	static Logger logger = Logger.getLogger(WorkPostProcessor.class);
	
	public static Work post(Work work) {
		decideItem(work);
		decideSize(work);
		decideBleed(work);
		return essentialItem(work);
	}
	
	private static Work essentialItem(Work work) {
		if(work.getItem() == null)
			work.setItem("known");
		if(work.getSize() == null)
			work.setSize("known");
		return work;
	}
	
	
	public static Work decideItem(Work work) {
		if(work.getItem() != null && work.getSize() == null) return work;
		String item = ItemParser.searchItem(work.getSize());
		return item != null ? setItem(work, item) : work;
	}
	private static Work setItem(Work work, String item) {
		work.setItem(item);
		return work;
	}
		
	
	public static Work decideSize(Work work) {
		if(work.getSize() != null || work.getItem() == null) return work;
		
		String item = work.getItem();
		
		switch(item) {
		case "대봉투" :
			return setSize(work, "388-515", 0);
		case "자켓봉투" :
			return setSize(work, "262-256", 0);
		case "다대봉투" :
			return setSize(work, "265-230", 0);
		
		case "명함" :
			return setSize(work, "90-50", 2);
		
		case "신분증" :
			return setSize(work, "88-56", 0);
			
		}
		return work;
		
	}
	// 사이즈에서 블리드값 뽑아내기
	public static Work decideBleed(Work work) {
		if(work.getBleed() != 0 || work.getSize() == null) return work;
		String size = work.getSize();
		
		switch(size) {
		case "92-52" :
			return setSize(work, "90-50", 2);
		case "299-212" :
			return setSize(work, "297-210", 2);
		case "212-299" :
			return setSize(work, "210-297", 2);
		default :
			return work;
		}
	}
	private static Work setSize(Work work, String size, int bleed) {
		work.setSize(size);
		work.setBleed(bleed);
		return work;
	}

}

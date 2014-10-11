package kr.co.koreanmagic.web.db.mybatis.support;

public enum WorkListTypes {
	
	NORMAL("일반"), TODAY_WORK("당일제작"), REWORK("재작업"), ESTIMATE("견적"), ONLY_DRAFT("시안작업"), FAST("긴급");
	
	private String name;
	
	private WorkListTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static WorkListTypes parseOf(String name) {
		switch(name) {
		case "일반" :
			return NORMAL;
		case "당일제작" : 
			 return TODAY_WORK;
		case "재작업" : 
			return REWORK;
		case "견적" : 
			return ESTIMATE;
		case "시안작업" : 
			return ONLY_DRAFT;
		default :
			throw new RuntimeException("WorkListType에 잘못된 파싱 데이터가 들어왔습니다. [" + name + "]");
		}
	}
}

package kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype;

public enum WorkStatus {

	WAITING("대기"), WORKING("시안작업"), CONFIRM("완료");
	
	private String type;
	
	private WorkStatus(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public static WorkStatus defaultType() {
		return WAITING;
	}
	
	public static WorkStatus get() {
		return defaultType();
	}
	public static WorkStatus get(String type) {
		if(type == null)
			return defaultType();
		for(WorkStatus t : WorkStatus.values()) {
			if(t.getType().contains(type))
				return t;
		}
		return defaultType();
	}
	
	@Override
	public String toString() {
		return getType();
	}
}

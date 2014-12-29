package kr.co.koreanmagic.hibernate3.mapper.domain.support.enumtype;

public enum CompanyType {
	
	CORPORATE("법인사업자"), GENERAL("일반과세자"), SIMPLE("간이과세자"), PERSON("개인"), ETC("기타");
	
	private String type;
	
	private CompanyType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public static CompanyType defaultType() {
		return GENERAL;
	}
	
	public static CompanyType get(String type) {
		if(type == null)
			return defaultType();
		for(CompanyType t : CompanyType.values()) {
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

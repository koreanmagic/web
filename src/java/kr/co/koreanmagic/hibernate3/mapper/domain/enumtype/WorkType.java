package kr.co.koreanmagic.hibernate3.mapper.domain.enumtype;

import kr.co.koreanmagic.hibernate3.mapper.domain.json.ToEnumType;

import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using=ToEnumType.class)
public enum WorkType {

	DEFAULT("일반"), TODAY("당일"), BUSSY("긴급");
	
	private String type;
	
	private WorkType(String type) {
		this.type = type;
	}
	
	@JsonValue
	public String getType() {
		return this.type;
	}
	
	public static WorkType defaultType() {
		return TODAY;
	}
	
	public static WorkType get() {
		return defaultType();
	}
	
	public static WorkType get(int type) {
		return WorkType.values()[type];
	}
	
	public static WorkType get(String type) {
		if(type == null)
			return defaultType();
		for(WorkType t : WorkType.values()) {
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

package kr.co.koreanmagic.hibernate3.mapper.domain.enumtype;

import kr.co.koreanmagic.hibernate3.mapper.domain.json.ToEnumType;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using=ToEnumType.class)
public enum DeliveryType {
	
	VISIT("방문"), DELIVER("직접배송"), PARCEL("택배");
	
	private String type;
	
	private DeliveryType(String type) {
		this.type = type;
	}
	
	
	public String getType() {
		return this.type;
	}
	
	public int ord() {
		return this.ordinal();
	}
	
	public static DeliveryType defaultType() {
		return VISIT;
	}
	
	public static DeliveryType get(String type) {
		if(type == null)
			return defaultType();
		for(DeliveryType t : DeliveryType.values()) {
			if(t.getType().contains(type))
				return t;
		}
		return defaultType();
	}
	
	public static DeliveryType get(int o) {
		switch(o) {
			case 0: return VISIT;
			case 1: return DELIVER;
			case 2: return PARCEL;
			default: return VISIT;
		}
	}
	
	@Override
	public String toString() {
		return getType();
	}

}

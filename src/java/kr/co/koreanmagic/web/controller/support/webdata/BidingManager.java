package kr.co.koreanmagic.web.controller.support.webdata;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

public class BidingManager<T> {
	
	private PropertyDescriptor[] des;
	
	public BidingManager() {
		
	}
	
	public BidingManager(Class<T> target) {
		des = BeanUtils.getPropertyDescriptors(target);
	}
	
	public T populate(T bean, Map<String, ?> values) throws Exception {
		Object value = null;
		for(PropertyDescriptor de : des) {
			value = values.get(de.getName());
			if(value != null) {
				value = convert(value, de.getPropertyType());
				ReflectionUtils.invokeMethod(de.getWriteMethod(), bean, value);
			}
		}
		return bean;
	}
	
	private Object convert(Object obj, Class<?> type) {
		if(type == Integer.class || type == int.class)
			return Integer.valueOf(obj.toString());
		return obj;
	}
	
}

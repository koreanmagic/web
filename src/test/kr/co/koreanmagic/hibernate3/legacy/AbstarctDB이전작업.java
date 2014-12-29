package kr.co.koreanmagic.hibernate3.legacy;

import java.io.BufferedWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import kr.co.koreanmagic.commons.KoJDBCUtils;
import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.commons.KoUtils;
import kr.co.koreanmagic.dao.GenericDao;
import kr.co.koreanmagic.service.GenericService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class AbstarctDB이전작업 {
	
	
	@Autowired ConversionService conversionService;
	@Autowired SessionFactory factory;
	@Autowired List<? extends GenericDao<?, ? extends Serializable>> daoList;
	@Autowired List<? extends GenericService<?, ? extends Serializable>> serviceList;
	
	
	protected static void log(Object obj) {
		System.out.println(obj);
	}
	
	/*
	 * --로깅 유틸
	 * 모든 필드값 나열
	 */
	protected<T> String printValues(T obj, Class<? super T> parent) {
		Field field = null;
		StringBuilder sb = new StringBuilder();
		Field[] fields = parent.getDeclaredFields();
		final String seperater = ", ";
		for(Field f : fields) {
			field = f;
			f.setAccessible(true);
			sb.append(f.getName()).append("/");
			try {
				sb.append($(f.get(obj))).append(seperater);
			} catch (Exception e) {
				log("error : " + field.getName());
				KoUtils.timeout();
			}
		}
		
		return sb.toString();
		
	}
	protected String $(Object obj) {
		return $(obj, "");
	}
	protected String $(Object obj, String str) {
		if(obj == null) return str;
		return obj.toString();
	}

	
	protected Session session() {
		return this.factory.getCurrentSession();
	}
	
	/*
	 * 모든 Service, Dao 객체를 List에 받아놓고,
	 * 엔터티 클래스를 키로 넣어주면 해당 서비스를 찾아준다.
	 */
	@SuppressWarnings("unchecked")
	protected<V extends GenericService<?, ? extends Serializable>, T, P extends Serializable> V findService(T entity) {
		for(GenericService<?, ? extends Serializable> service : serviceList) {
			if(service.getServiceClass().equals(entity))
				return (V)service;
		}
		throw new RuntimeException(entity + "에 해당하는 service가 없습니다.");
	}
	
	@SuppressWarnings("unchecked")
	protected<V extends GenericDao<?, ? extends Serializable>, T, P extends Serializable> V findDao(T entity) {
		for(GenericDao<?, ? extends Serializable> dao : daoList) {
			if(dao.getPersistentClass().equals(entity))
				return (V)dao;
		}
		throw new RuntimeException(entity + "에 해당하는 dao가 없습니다.");
	}
	
	
	
	/*
	 * columns와 tableName을 설정해주면 모든 걸 뽑아서 csv로 저장해준다.
	 */
	protected int loadDate(Path saveFile, String[] columns, String tableName) throws Exception {
		
		int[] count = new int[1];
		final String SEPERATOR = "\r\n";
		
		log(factory);
		
		try(BufferedWriter writer = Files.newBufferedWriter(saveFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
			
			StringBuilder sb = new StringBuilder();
			
			factory.getCurrentSession().doWork(con -> {
				
				try {
					
					String sql = KoJDBCUtils.createSelectSQL(columns, tableName, null);
					PreparedStatement ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
				
					writer.append("#" + KoStringUtils.join(",", columns)).append(SEPERATOR);
					
					int coun = 0, len = columns.length;
					String[] values = null;
					
					while(rs.next()) {
						
						values = new String[len];
						for(int i=0; i<len; i++) {
							values[i] = rs.getString(columns[i]);
						}
						
						writer.append(KoStringUtils.join(",", values)).append(SEPERATOR);
						
						coun++;
					}
					
					count[0] = coun;
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
			});
		}
		
		return count[0];
		
	}
}

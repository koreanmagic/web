package kr.co.koreanmagic.hibernate.mapper.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import kr.co.koreanmagic.hibernate.mapper.category.ItemCategory;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;


public class CategoryUniqueNum extends CustomerGenerator {

	@Override
	Serializable generate(SessionImplementor session, Connection con, Object object) throws HibernateException {
		
		/*ItemCategory ic = (ItemCategory)object;
		ItemCategory.PrimaryKey result = new ItemCategory.PrimaryKey();
		
		String categoryName = ic.categoryName();;
		result.setCategoryName(categoryName);
		
		try(PreparedStatement ps
				= con.prepareStatement("SELECT COUNT(*) FROM item_category WHERE category_name = ?");) {
			
			ps.setString(1, categoryName);
			ResultSet resultSet = ps.executeQuery();
			Long num = null;
			
			if(resultSet.next()) {
				num = resultSet.getLong(1);
				result.setNum(num + 1l);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
		
		return null;
	}


}

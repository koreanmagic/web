package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

/* 사업체 분류 */

@Entity
@Table(name="biz_class")

@NamedNativeQuery(
name = "bizclass",
query = "SELECT b.id id, b.biz_class bizClasss, b.memo memo  FROM biz_class b WHERE b.id = :id",
resultClass = BizClass.class,
resultSetMapping="result"
)

@SqlResultSetMapping(name="result",
	entities=
		@EntityResult(entityClass=BizClass.class,
					fields = {	@FieldResult(name="id", column = "id"),
        						@FieldResult(name="bizClass", column = "biz_class"),
        						@FieldResult(name="memo", column = "memo"),
       							})
)
public class BizClass {
	
	private Long id;
	private String bizClass;
	private String memo;
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false, name="biz_class")
	public String getBizClass() {
		return bizClass;
	}
	public void setBizClass(String bizClass) {
		this.bizClass = bizClass;
	}
	
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BizClass other = (BizClass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getBizClass();
	}
	
	
	/*
	 * Utils
	 */
	private static Set<BizClass> list;
	
	public static void setClasses(Collection<BizClass> list) {
		getList().addAll(list);
	}
	
	public static void addClasses(BizClass bc) {
		getList().add(bc);
	}
	
	public static Set<BizClass> getList() {
		if(BizClass.list == null)
			BizClass.list = new HashSet<>();
		return BizClass.list;
	}
	
	/*
	 * 찾다가 없으면 맨 마지막 값을 내보낸다.
	 */
	public static BizClass find(int id) {
		BizClass result = null;
		for(BizClass bc : getList()) {
			result = bc;
			if(result.getId() == id)
				return result;
		}
		return result;
	}

	
	public static BizClass find(String className) {
		BizClass result = null;
		for(BizClass bc : getList()) {
			result = bc;
			if(result.getBizClass().contains(className))
				return result;
		}
		return result;
	}
	
	

}

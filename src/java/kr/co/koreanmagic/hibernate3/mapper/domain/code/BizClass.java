package kr.co.koreanmagic.hibernate3.mapper.domain.code;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*@NamedNativeQuery(
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
)*/

/* 사업체 분류 */

@Entity
@Table(name="biz_class")
public class BizClass {
	
	private Long id;
	private String name;
	private String memo;
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false, name="biz_class")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return getName();
	}
	
	

}

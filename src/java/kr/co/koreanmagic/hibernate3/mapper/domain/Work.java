package kr.co.koreanmagic.hibernate3.mapper.domain;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.co.koreanmagic.hibernate3.mapper.domain.category.ItemCategory;
import kr.co.koreanmagic.hibernate3.mapper.domain.embeddable.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.joda.time.format.DateTimeFormat;

@Entity
@Table(name="work")
public class Work {
	
	private Long id;
	private WorkGroup workGroup;
	private ItemCategory itemCategory;
	private String itemMemo;
	
	
	private Date insertTime;
	private Timestamp updateTime;
	
	private Subcontractor subconstractor;
	
	private Integer cost;
	private Integer price;
	
	private Size size;
	private Size bleedSize;
	private String unit;
	

	private Integer count;		// 건수
	private String number;		// 수량
	
	
	private Date deliveryDate;
	private Address deliveryAddress;
	private String deliveryMemo;
	
	private String memo;

	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="work_group_id")
	public WorkGroup getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(WorkGroup workGroup) {
		this.workGroup = workGroup;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="item_category_id")
	public ItemCategory getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	public String getItemMemo() {
		return itemMemo;
	}
	public void setItemMemo(String itemMemo) {
		this.itemMemo = itemMemo;
	}

	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subconstractor_id")
	public Subcontractor getSubconstractor() {
		return subconstractor;
	}
	public void setSubconstractor(Subcontractor subconstractor) {
		this.subconstractor = subconstractor;
	}

	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}

	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="bleed_width")),
		@AttributeOverride(name="height", column=@Column(name="bleed_height")),
	})
	public Size getBleedSize() {
		return bleedSize;
	}
	public void setBleedSize(Size bleedSize) {
		this.bleedSize = bleedSize;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@org.hibernate.annotations.Cascade({CascadeType.SAVE_UPDATE})
	@JoinColumn(name="delivery_address")
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	
	public String getDeliveryMemo() {
		return deliveryMemo;
	}
	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	@Override
	public String toString() {
		return getInsertTime().toString();
	}
	

}

package kr.co.koreanmagic.hibernate3.DB이전.domain;

import java.sql.Timestamp;

public class Work {
	private String id;

    private Timestamp insertTime;
    private Timestamp insert_time;

    private WorkCondition condition;
    private Integer con_id;

    private String customer;

    private String item;

    private String itemType;
    private String item_type;

    private Short count;

    private String number;

    private String size;

    private String unit;

    private Integer bleed;

    private String memo;

    private String tag;

    private Integer cost;

    private Integer price;

    private Timestamp updateTime;
    private Timestamp update_time;

    private String delivery;

    private String subcontract;

    private Timestamp deliveryTime;

    private String itemMemo;

    private Integer readCount;

    private String bleedSize;

    private String deliveryMemo;

    private WorkListType listType;

    private Boolean readCheck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }
    

    public Timestamp getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}

	public WorkCondition getCondition() {
        return condition;
    }
    public void setCondition(WorkCondition condition) {
        this.condition = condition;
    }
    
    public Integer getCon_id() {
		return con_id;
	}
	public void setCon_id(Integer con_id) {
		this.con_id = con_id;
	}

	public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
    }
    
    public String getItem_type() {
		return item_type;
	}
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}

	public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getBleed() {
        return bleed;
    }

    public void setBleed(Integer bleed) {
        this.bleed = bleed;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    
    public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

	public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery == null ? null : delivery.trim();
    }

    public String getSubcontract() {
        return subcontract;
    }

    public void setSubcontract(String subcontract) {
        this.subcontract = subcontract == null ? null : subcontract.trim();
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getItemMemo() {
        return itemMemo;
    }

    public void setItemMemo(String itemMemo) {
        this.itemMemo = itemMemo == null ? null : itemMemo.trim();
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getBleedSize() {
        return bleedSize;
    }

    public void setBleedSize(String bleedSize) {
        this.bleedSize = bleedSize == null ? null : bleedSize.trim();
    }

    public String getDeliveryMemo() {
        return deliveryMemo;
    }

    public void setDeliveryMemo(String deliveryMemo) {
        this.deliveryMemo = deliveryMemo == null ? null : deliveryMemo.trim();
    }

    public WorkListType getListType() {
        return listType;
    }

    public void setListType(WorkListType listType) {
        this.listType = listType;
    }

    public Boolean getReadCheck() {
        return readCheck;
    }

    public void setReadCheck(Boolean readCheck) {
        this.readCheck = readCheck;
    }
    
    StringBuilder sb = new StringBuilder();
    
    @Override // 140417001_거래처명_품목(재질)_[건수-수량]_사이즈|단위
    public String toString() {
    	String result = null;;
    	sb = new StringBuilder();
    	
    	if((result = getId()) != null) // 아이디
    		sb.append(result);
    	if((result = getCustomer()) != null) // 거래처명
    		underLine().append(result);
    	if((result = getItem()) != null && result.length() > 0) { // 품목이 있을때만 재질 설정
    		underLine().append(result);
    		if((result = getItemType()) != null && result.length() > 0) // 재질
        		sb.append("(").append(result).append( ")");
    	}
    	
    	// 건수만 있으면 [건수] // 수량도 있으면 [건수-수량]
    	sb.append("_[").append(checkNum(getCount()));
    	if(getNumber() != null && getNumber().length() > 0)
    		sb.append("-").append(getNumber()).append("]"); 	// 건수-수량
    	else
    		sb.append("]");
    	
    	if(getSize() != null && getSize().length() > 0) {
    		underLine().append(getSize()).append(getUnit());
    	}
    	
    	return sb.toString();
    }
    
    private String checkNum(Integer integer) {
    	if(integer == null) return "0";
    	return integer.toString();
    }
    
    
    private StringBuilder underLine() {
    	return sb.append("_");
    }
}
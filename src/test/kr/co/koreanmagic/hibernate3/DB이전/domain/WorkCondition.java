package kr.co.koreanmagic.hibernate3.DB이전.domain;

public class WorkCondition implements Comparable<WorkCondition> {
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

	@Override
	public int compareTo(WorkCondition other) {
		int otherId = other.getId();
		if(otherId > id) return -1;
		else if(otherId < id) return 1;
		return 0;
	}
}
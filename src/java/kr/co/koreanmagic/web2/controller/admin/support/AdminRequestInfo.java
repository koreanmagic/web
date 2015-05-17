package kr.co.koreanmagic.web2.controller.admin.support;

import kr.co.koreanmagic.web2.controller.RequestInfo;

public class AdminRequestInfo implements RequestInfo {

	private String group;
	private String command;
	private String view;
	private String model;
	private String query;
	
	public AdminRequestInfo(String[] paths) {
		int i = 1, l = paths.length;
		
		this.group = 	paths[0];
		this.command = 	l > i ? paths[i++] : null;
		this.view = 	l > i ? paths[i++] : null;
		this.model = 	l > i ? paths[i++] : null;
	}
	
	public AdminRequestInfo setCommand(String command) {
		this.command = command;
		return this;
	}
	@Override
	public String getCommand() {
		return this.command;
	}
	
	public AdminRequestInfo setGroup(String group) {
		this.group = group;
		return this;
	}
	@Override
	public String getGroup() {
		return this.group;
	}
	
	public AdminRequestInfo setView(String view) {
		this.view = view;
		return this;
	}
	@Override
	public String getView() {
		return this.view;
	}
	
	public AdminRequestInfo setModel(String model) {
		this.model = model;
		return this;
	}
	@Override
	public String getModel() {
		return this.model;
	}
	
	public AdminRequestInfo setQuery(String name) {
		this.query = name;
		return this;
	}
	@Override
	public String getQuery() {
		return this.query;
	}


	
}

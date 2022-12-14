package com.mwgroup.hydra.model;

import java.util.List;

public class Searches {

	private String search;
	private String theme;
	private String channel;
	private String sheet;
	private Rules rules;
	private List<String> users;
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public Rules getRules() {
		return rules;
	}
	public void setRules(Rules rules) {
		this.rules = rules;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	
}

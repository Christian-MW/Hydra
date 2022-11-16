package com.mwgroup.hydra.model;

import java.util.List;

public class Rules {

	private long minFollowers;
	private long maxFollowers;
	private String verifiedUser;
	private List<String> typeTweet;
	private List<String> language;
	private List<String> location;
	private long  minNumReactions;
	private long  maxNumReactions;
	
	public long getMinFollowers() {
		return minFollowers;
	}
	public void setMinFollowers(long minFollowers) {
		this.minFollowers = minFollowers;
	}
	public long getMaxFollowers() {
		return maxFollowers;
	}
	public void setMaxFollowers(long maxFollowers) {
		this.maxFollowers = maxFollowers;
	}
	public String getVerifiedUser() {
		return verifiedUser;
	}
	public void setVerifiedUser(String verifiedUser) {
		this.verifiedUser = verifiedUser;
	}
	public List<String> getTypeTweet() {
		return typeTweet;
	}
	public void setTypeTweet(List<String> typeTweet) {
		this.typeTweet = typeTweet;
	}
	public List<String> getLanguage() {
		return language;
	}
	public void setLanguage(List<String> language) {
		this.language = language;
	}
	public List<String> getLocation() {
		return location;
	}
	public void setLocation(List<String> location) {
		this.location = location;
	}
	public long getMinNumReactions() {
		return minNumReactions;
	}
	public void setMinNumReactions(long minNumReactions) {
		this.minNumReactions = minNumReactions;
	}
	public long getMaxNumReactions() {
		return maxNumReactions;
	}
	public void setMaxNumReactions(long maxNumReactions) {
		this.maxNumReactions = maxNumReactions;
	}
	
	
}

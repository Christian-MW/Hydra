package com.mwgroup.hydra.dto.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mwgroup.hydra.model.*;

public class SendPostTWRequest {
	public HashMap<String, Object> data;
	public HashMap<String, Object> includes;
	public ArrayList<Object> matching_rules;
	public HashMap<String, Object> getData() {
		return data;
	}
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	public HashMap<String, Object> getIncludes() {
		return includes;
	}
	public void setIncludes(HashMap<String, Object> includes) {
		this.includes = includes;
	}
	public ArrayList<Object> getMatching_rules() {
		return matching_rules;
	}
	public void setMatching_rules(ArrayList<Object> matching_rules) {
		this.matching_rules = matching_rules;
	}
	
}

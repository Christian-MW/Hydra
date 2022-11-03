package com.mwgroup.hydra.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mwgroup.hydra.dto.request.*;
import com.mwgroup.hydra.dto.response.*;
import com.mwgroup.hydra.service.HydraService;


@RestController
@RequestMapping(value="/Hydra")
public class HydraRest {
	
	@Autowired
	HydraService hydraService;

	@PostMapping(value="/sendPost/tweet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SendPostTWResponse sendPostTweet(@RequestBody SendPostTWRequest request) {
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
		
		return hydraService.sendPostTweet(request);
	}
	
	
	
	
	@GetMapping(value="/sendPost/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getDataTest() {
		
		Gson gson = new Gson();
		/*ArrayList<Object> ArrayListIt = new ArrayList<Object>();
		Object tweetIt = new Object();
		TweetStream item = new TweetStream();
		item.setData("Element");
		tweetIt = item;
		ArrayListIt.add(tweetIt);
		item.setData("Element2");
		tweetIt = item;
		ArrayListIt.add(tweetIt);
		System.out.println(gson.toJson(ArrayListIt));*/
		
		
		SendPostTWRequest req = new SendPostTWRequest();
		
		
		System.out.println(gson.toJson(req));
		System.out.println(gson.toJson(req));
	}
}

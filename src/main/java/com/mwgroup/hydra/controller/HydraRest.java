package com.mwgroup.hydra.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
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
import com.mwgroup.hydra.impl.HydraImpl;
import com.mwgroup.hydra.service.HydraService;


@RestController
@RequestMapping(value="/Hydra")
public class HydraRest {
	
	@Autowired
	HydraService hydraService;
	private static Logger log = Logger.getLogger(HydraImpl.class);

	@PostMapping(value="/sendPost/tweet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SendPostTWResponse sendPostTweet(@RequestBody SendPostTWRequest request) {
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
		System.out.println("Iniciando");
		try {
			ExecutorService exec1 = Executors.newSingleThreadExecutor();
            exec1.submit(() -> {
            	
            	System.out.println("Iniciando la validación del mensaje");
    			hydraService.sendPostTweet(request);
    			System.out.println("Finalizando la validación del mensaje");
                exec1.shutdown();
            }); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Respondiendo");
		SendPostTWResponse result = new SendPostTWResponse();
		result.setMessage("OK");
		result.setCode(200);
		return result;
	}
	
	
	@PostMapping(value="/validatePost/campaign", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SendPostTWResponse validatePostCampaign(@RequestBody SendPostTWRequest request) {
		SendPostTWResponse result = new SendPostTWResponse();
		
		try {
			ExecutorService exec1 = Executors.newSingleThreadExecutor();
            exec1.submit(() -> {
            	
            	System.out.println("Iniciando la validación del mensaje");
    			hydraService.validatePostCampaign(request);
    			System.out.println("Finalizando la validación del mensaje");
                exec1.shutdown();
            }); 
			
			return result;
		} catch (Exception ex) {
			log.error("############### ERROR __sendPostTweet__: ");
			log.error(ex.getMessage());
			result.setCode(500);
			result.setMessage("ERROR_:" +ex.getMessage());
			return result;
		}
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

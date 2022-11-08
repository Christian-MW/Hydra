package com.mwgroup.hydra.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mwgroup.hydra.model.SendPostModel;

@Component
public class Utilities {
    @Value("${url.send.post}")
    private String URL_SEND_POST;
    @Value("${token.post.tw}")
    private String TOKEN_TW;
    @Value("${url.json.searches}")
    private String URL_SEARCHES;
	
	public Boolean sendPostTweet(SendPostModel req) {
		Boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost request = new HttpPost(URL_SEND_POST);
		    request.addHeader("content-type", "application/json");
		    request.addHeader("Accept","application/json");
		    
		    req.setToken(TOKEN_TW);
		    req.setChannel("telegram");
		    req.setType("chat");
		    req.setFrom("");
		    request.setEntity((HttpEntity) req);
		    ResponseHandler<String> responseHandler = new BasicResponseHandler();
		    
		    String response = httpClient.execute(request, responseHandler);
		    System.out.println(response);
		    
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject  jobject = jelement.getAsJsonObject();
			if(jobject.get("success").getAsBoolean()) {
				System.out.println("");
			}
			
			return result;
		} catch (Exception ex) {
			return result;
		}
	}
	public String getDataClients() {
		String result = "";
		try {

		    URL url = new URL(URL_SEARCHES);
		    URLConnection request = url.openConnection();
		    request.connect();
		    // Convert to a JSON object to print data
		    JsonParser jp = new JsonParser(); 
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		    
			return result;
		} catch (Exception ex) {
			return result;
		}
	}
	
	public String cleanText(String text) {
		String result = "";
		try {
			text = text.toLowerCase();
			text.replace("Á", "A");
			text.replace("É", "E");
			text.replace("Í", "I");
			text.replace("Ó", "O");
			text.replace("Ú", "U");
			text.replace("á", "a");
			text.replace("é", "e");
			text.replace("í", "i");
			text.replace("ó", "o");
			text.replace("ú", "u");
			
			return result;
		} catch (Exception e) {
			return result;
		}
	}
}

package com.mwgroup.hydra.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.http.*; 

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mwgroup.hydra.model.Searches;
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
			System.out.println("");
			System.out.println("sendPostTweet");
			System.out.println(URL_SEND_POST);
			req.setToken("");
		    req.setChannel("telegram");
		    req.setType("chat");
		    req.setFrom("");
		    
			var request = HttpRequest.newBuilder()
					.uri(URI.create(URL_SEND_POST))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))				
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			System.out.println(response.statusCode());
			System.out.println(response.body());
			System.out.println(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			var sended = map.get("sended");
			if(!(Boolean)sended) {
				System.out.println("No fue posible enviar el mensaje");
			}

			return result;
		} catch (Exception ex) {
			System.out.println(ex);
			return result;
		}
	}
	public List<Searches> getDataClients() {
		List<Searches> result = new ArrayList<Searches>();
		try {

		    var rq = HttpRequest.newBuilder()
					.uri(URI.create(URL_SEARCHES))
					.header("Content-type", "application/json")
					.GET()				
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(rq, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.statusCode());
			System.out.println(response.body().getClass());
			Type listType = new TypeToken<List<Searches>>() {}.getType();
			result = new Gson().fromJson(response.body(), listType);
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
			
			return text;
		} catch (Exception e) {
			return result;
		}
	}
	
	
	public StringBuilder getMessage(String date, String text, String url) {
		StringBuilder sb = new StringBuilder();
		try {
			
			sb.append("FECHA Y HORA: \n"+date );
			sb.append("\n" );
			sb.append("\nTEXTO: \n"+text );
			sb.append("\n" );
			sb.append("\nLIGA: \n"+url );
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return sb;
	}
}

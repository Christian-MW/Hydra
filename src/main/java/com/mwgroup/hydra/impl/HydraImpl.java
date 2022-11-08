package com.mwgroup.hydra.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mwgroup.hydra.dto.request.*;
import com.mwgroup.hydra.dto.response.*;
import com.mwgroup.hydra.model.*;
import com.mwgroup.hydra.service.HydraService;
import com.mwgroup.hydra.utils.Utilities;

@Service("HydraImpl")
public class HydraImpl implements HydraService{
	
	 @Autowired
	 Utilities utilities;
	
	public SendPostTWResponse sendPostTweet(SendPostTWRequest request) {
		SendPostTWResponse result = new SendPostTWResponse();
		Gson gson = new Gson();
		String pathTW = "https://twitter.com/";
		String authorid = "";
		String idioma = "";
		String postid = "";
		String message = "";
		Boolean IsOriginal = true;
		Boolean IsRT = false;
		Boolean IsQT = false;
		Boolean IsReeply = false;
		Boolean isVerificated = false;
		String locationUser = "";
		String urlPost = "";
		Date datePost = new Date();
		
		Integer countPMetrics = 0;
		Integer followersUser = 0;
		
		Integer likeCountPost = 0;
		Integer quoteCountPost = 0;
		Integer replyCountPost = 0;
		Integer retweetCountPost = 0;
		Integer AllCountPost = 0;
		
		String channelClient = "";
		Integer valPost = 0;
		try {

			utilities.getDataClients();
			//Primer objeto DATA
			for (Entry<String, Object> entry : request.getData().entrySet()) {
				if (entry.getKey() == "id")
					postid = entry.getValue().toString();
				if (entry.getKey() == "lang")
					idioma = entry.getValue().toString();
				if(entry.getKey() == "text")
					message = entry.getValue().toString();
				if(entry.getKey() == "author_id")
					authorid = entry.getValue().toString();
				if(entry.getKey() == "created_at") {
					 SimpleDateFormat formDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					 datePost=formDate.parse(entry.getValue().toString());
				}
				
				//##############Validación de idioma (puros "es")
				if (idioma.equals("es"))
					valPost++;
				
				//Validar si existe el objeto referenced_tweets
				if (entry.getKey() == "referenced_tweets") {
					IsOriginal = false;
					ArrayList<HashMap<String, String>> objRefT = new ArrayList<HashMap<String, String>>();
					objRefT = (ArrayList<HashMap<String, String>>) entry.getValue();
					
					if(objRefT.get(0).get("type").toString().equals("retweeted")) 
						IsRT = true;
					if(objRefT.get(0).get("type").toString().equals("quoted")) 
						IsQT = true;
					if(objRefT.get(0).get("type").toString().equals("replied_to")) 
						IsReeply = true;
				}
				
				//##############Validación de si un tweet es original
				/*if (IsOriginal)
					valPost++;*/
			}
			
			
			
			//Segundo objeto includes
			for (Entry<String, Object> entry : request.getIncludes().entrySet()) {
				ArrayList<HashMap<String, Object>> itemTweet = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> itemUsers = new ArrayList<HashMap<String, Object>>();
				if(entry.getKey() == "tweets"){
					itemTweet = (ArrayList<HashMap<String, Object>>) entry.getValue();
					for ( HashMap<String, Object> tweetUs : itemTweet) {
						String tweetID = tweetUs.get("author_id").toString();
						if (authorid.equals(tweetID)) {
							HashMap<String, Object> publicM = new HashMap<String, Object>();
							publicM = (HashMap<String, Object>) tweetUs.get("public_metrics");
							likeCountPost = (Integer) publicM.get("like_count");
							quoteCountPost = (Integer) publicM.get("quote_count");
							replyCountPost = (Integer) publicM.get("reply_count");
							retweetCountPost = (Integer) publicM.get("retweet_count");
							AllCountPost = likeCountPost + quoteCountPost + replyCountPost + retweetCountPost;
							
							//##############Validación si el post tiene mas de 10 reacciones en total
							if (AllCountPost >= 10)
								valPost++;
						}
					}
					
					System.out.println("=>itemTweet: " + itemTweet);
				}
				
				if(entry.getKey() == "users"){
					itemUsers = (ArrayList<HashMap<String, Object>>) entry.getValue();
					for ( HashMap<String, Object> itmUs : itemUsers) {
						String usrID = itmUs.get("id").toString();
						isVerificated = (Boolean) itmUs.get("verified");
						
						//##############Validación de la cuenta del usuario
						if (isVerificated)
							valPost++;
						
						urlPost = pathTW + itmUs.get("username").toString() + "/status/" + postid;
						if (authorid.equals(usrID)) {
							//Esta es la data del usuario que posteo el TWEET
							HashMap<String, Object> publicM = new HashMap<String, Object>();
							publicM = (HashMap<String, Object>) itmUs.get("public_metrics");
							followersUser = (Integer) publicM.get("followers_count");
							
							//##############Validación de que el usuario tenga más de 10000 seguidores
							if (followersUser >= 10000)
								valPost++;
							
							try {
								//ValidateLocationUser
								String location = utilities.cleanText(itmUs.get("location").toString());
								
								//##############Validación de que el usuario sea de México
								if (location.contains("mexico"))
									valPost++;
								
								locationUser = location;
							} catch (Exception e) {
								locationUser = "";
							}
							System.out.println("=>followers_count: " + publicM.get("followers_count").toString());
							System.out.println("=>following_count: " + publicM.get("following_count").toString());
						}
					}
				}
				System.out.println("=>Entry: " + entry.getValue().toString());
			}
			
			//Tercer objeto matching_rules
			for (Object entry : request.getMatching_rules()) {
				HashMap<String, Object> itemMR = new HashMap<String, Object>();
				itemMR = (HashMap<String, Object>) entry;
				channelClient = itemMR.get("tag").toString().split(":")[1];
				System.out.print("=> Client and Channel: " + itemMR.get("tag"));
			}
			
			if (valPost >= 5) {
				//Envío del Tweet al canal
				SendPostModel itemTW = new SendPostModel();
				itemTW.setText(message);
				itemTW.setTo(channelClient);
				utilities.sendPostTweet(itemTW);
				
				result.setMessage("OK");
				result.setCode(200);
				return result;
			}
			
			result.setMessage("NOT-SEND");
			result.setCode(202);
			return result;
		} catch (Exception e) {
			
			result.setMessage("Error");
			result.setCode(500);
			return result;
		}
	}
}

package com.mwgroup.hydra.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
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
	 private static Logger log = Logger.getLogger(HydraImpl.class);
	 
	 @Value("${url.get.search}")
	 private String URL_GET_SEARCH;

	 @Value("${url.campaign.user.update}")
	 private String URL_CAMPAING_USER_UPDATE;
	
	public void sendPostTweet(SendPostTWRequest request) {
		log.info("#############################___sendPostTweet");
		
		SendPostTWResponse result = new SendPostTWResponse();
		Gson gson = new Gson();
		String pathTW = "https://twitter.com/";
		String authorid = "";
		String idioma = "";
		String postid = "";
		String message = "";
		String typeTweet = "original";
		Boolean IsOriginal = true;
		Boolean IsRT = false;
		Boolean IsQT = false;
		Boolean IsReeply = false;
		Boolean isVerificated = false;
		String locationUser = "";
		String urlPost = "";
		Date datePost = new Date();
		String nickname = "";
		
		Integer countPMetrics = 0;
		Integer followersUser = 0;
		
		Integer likeCountPost = 0;
		Integer quoteCountPost = 0;
		Integer replyCountPost = 0;
		Integer retweetCountPost = 0;
		Integer AllCountPost = 0;
		
		String channelClient = "";
		Integer valPost = 0;
		
		String created_at ="";
		String text="";
		String url="https://twitter.com/{{username}}/status/{{idTweet}}";
		Map <String, Object> dataObtenida = new HashMap<String,Object>();
		try {

			List<Searches> searches =utilities.getDataClients(); 
			//Searches src = new Searches();
			List<Searches> listMatchingRulesSrc= new ArrayList<Searches>();
			String themeAndChannel ="";
			//Tercer objeto matching_rules
			for (Object entry : request.getMatching_rules()) {
				HashMap<String, Object> itemMR = new HashMap<String, Object>();
				itemMR = (HashMap<String, Object>) entry;
				themeAndChannel = itemMR.get("tag").toString();
				channelClient = itemMR.get("tag").toString().split(":")[1];
				System.out.print("=> Client and Channel: " + itemMR.get("tag"));
				
				for (int i = 0; i < searches.size(); i++) {
					try {
						//Busquedas sin sheet
						if(themeAndChannel.equals(searches.get(i).getTheme()+":"+searches.get(i).getChannel())) {
							listMatchingRulesSrc.add(searches.get(i));
							break;
						}
						//Busquedas con sheet campañas
						if(themeAndChannel.equals(searches.get(i).getTheme()+":"+searches.get(i).getChannel()+":"+ searches.get(i).getSheet())) {
							listMatchingRulesSrc.add(searches.get(i));
							break;
						}
					}catch(Exception ex) {
						log.info("Error al revisar camparar el tag");
						log.info(new Gson().toJson(searches.get(i)));
						log.error(ex);
					}
				}
			}
			
			log.info("Total de búsquedas con las que el twit coincide");
			log.info(new Gson().toJson(listMatchingRulesSrc));
			
			for (Searches src : listMatchingRulesSrc) {
				channelClient = src.getChannel();
	
				System.out.println(searches);
				//Primer objeto DATA
				for (Entry<String, Object> entry : request.getData().entrySet()) {
					if (entry.getKey() == "id") {
						postid = entry.getValue().toString();
						dataObtenida.put("postid", postid);
					}
					if (entry.getKey() == "lang")
						idioma = entry.getValue().toString();
					if(entry.getKey() == "text")
						message = entry.getValue().toString();
					if(entry.getKey() == "author_id") {
						authorid = entry.getValue().toString();
						dataObtenida.put("authorid", authorid);
					}
					if(entry.getKey() == "created_at") {
						 SimpleDateFormat formDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						 datePost=formDate.parse(entry.getValue().toString());
						 created_at = entry.getValue().toString().substring(0, 19).replace("T", " ");
					}
							
					//Validar si existe el objeto referenced_tweets
					if (entry.getKey() == "referenced_tweets") {
						IsOriginal = false;
						ArrayList<HashMap<String, String>> objRefT = new ArrayList<HashMap<String, String>>();
						objRefT = (ArrayList<HashMap<String, String>>) entry.getValue();
						
						typeTweet = objRefT.get(0).get("type").toString();						
					}
					
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
								
								dataObtenida.put("publicM", publicM);
															
							}
						}
						
						System.out.println("=>itemTweet: " + itemTweet);
					}
					
					if(entry.getKey() == "users"){
						itemUsers = (ArrayList<HashMap<String, Object>>) entry.getValue();
						for ( HashMap<String, Object> itmUs : itemUsers) {
							String usrID = itmUs.get("id").toString();		
							
							if (authorid.equals(usrID)) {
								
								nickname = itmUs.get("username").toString();
								dataObtenida.put("nickname", nickname);
								
								urlPost = pathTW + itmUs.get("username").toString() + "/status/" + postid;
								dataObtenida.put("urlPost", urlPost);
								//Esta es la data del usuario que posteo el TWEET
								HashMap<String, Object> publicM = new HashMap<String, Object>();
								publicM = (HashMap<String, Object>) itmUs.get("public_metrics");
								followersUser = (Integer) publicM.get("followers_count");
								isVerificated = (Boolean) itmUs.get("verified");
								url = url.replace("{{username}}", (String) itmUs.get("username"));
								dataObtenida.put("userPublicMetrics", publicM);
								try {
									//ValidateLocationUser
									if(Strings.isEmpty(locationUser)) {
										String location = utilities.cleanText(itmUs.get("location").toString());
																				
										locationUser = location;
									}
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
				
				
				dataObtenida.put("Location", locationUser);
				dataObtenida.put("Languaje", idioma);
				dataObtenida.put("TtypeTweet", typeTweet);
				dataObtenida.put("VerifiedUser", isVerificated);
				
				
				int valToComply = 0;
				valPost = 0;
				boolean isOK = false;
				String rulesFails = "";
				//Verificar localidad
				if(src.getRules().getLocation().size() > 0) {
					valToComply++;
					isOK = false;
					for (int i = 0; i < src.getRules().getLocation().size(); i++) {
						if(locationUser.contains(src.getRules().getLocation().get(i))) {
							valPost++;
							isOK = true;
							break;
						}
					}
					if(!isOK)
						rulesFails +="Location";
				}
				
				//Verificar lenguaje
				if(src.getRules().getLanguage().size() > 0) {
					valToComply++;
					isOK = false;
					for (int i = 0; i < src.getRules().getLanguage().size(); i++) {
						if(src.getRules().getLanguage().get(i).equals(idioma)) {
							valPost++;
							isOK = true;
							break;
						}
					}
					if(!isOK)
						rulesFails +=" Languaje";
				}
				
				//Verificar typo de tweet si es origina, rt, qt etc
				if(src.getRules().getTypeTweet().size() > 0) {
					valToComply++;
					isOK= false;
					for (int i = 0; i < src.getRules().getTypeTweet().size(); i++) {
						if(src.getRules().getTypeTweet().get(i).equals(typeTweet)) {
							valPost++;
							isOK =true;
							break;
						}
					}
					if(!isOK)
						rulesFails +=" TtypeTweet";
				}
				
				//Verificar tipo de cuenta si esta o no verificada 
				if(Strings.isNotEmpty(src.getRules().getVerifiedUser())) {
					valToComply++;
					isOK = false;
					if(isVerificated.toString().equals(src.getRules().getVerifiedUser()) || 
							src.getRules().getVerifiedUser().toLowerCase().equals("all")
							||src.getRules().getVerifiedUser().toLowerCase().equals("todos")) {
						valPost++;
						isOK = true;
					}
					
					if(!isOK)
						rulesFails +=" VerifiedUser";
				
				}
				
				//Verificar regla de seguidores en cuenta
				if(src.getRules().getMinFollowers() > 0) {
					valToComply++;
					isOK = false;
					if(src.getRules().getMaxFollowers() > 0) {
						if(src.getRules().getMinFollowers() <= src.getRules().getMaxFollowers()) {
							//Sea plica la regla del between 
							if(followersUser >= src.getRules().getMinFollowers() &&
									src.getRules().getMaxFollowers() >= followersUser) {
								valPost++;
								isOK = true;
							}
						}else {
							//Se aplica la regla que las reaccion sean mayor o igual al minimo de reacciones definido
							if(followersUser >= src.getRules().getMinFollowers()) {
								valPost++;
								isOK = true;
							}
						}
					}else {
						//Se aplica la regla que las reaccion sean mayor o igual al minimo de reacciones definido
						if(followersUser >= src.getRules().getMinFollowers()) {
							valPost++;
							isOK = true;
						}
					}
					
					if(!isOK)
						rulesFails +=" Followers";
				}
				
				//Verificar minimo de reacciones
				if(src.getRules().getMinNumReactions() > 0) {
					valToComply++;
					isOK = false;
					if(src.getRules().getMaxNumReactions() > 0) {
						if(src.getRules().getMinNumReactions() <= src.getRules().getMaxNumReactions()) {
							//Sea plica la regla del between 
							if(AllCountPost >= src.getRules().getMinNumReactions() &&
									src.getRules().getMaxNumReactions() >= AllCountPost) {
								valPost++;
								isOK = true;
							}
						}else {
							//Se aplica la regla que las reaccion sean mayor o igual al minimo de reacciones definido
							if(AllCountPost >= src.getRules().getMinNumReactions()) {
								valPost++;
								isOK = true;
							}
						}
					}else {
						//Se aplica la regla que las reaccion sean mayor o igual al minimo de reacciones definido
						if(AllCountPost >= src.getRules().getMinNumReactions()) {
							valPost++;
							isOK = true;
						}
					}

					if(!isOK)
						rulesFails +=" Reactions";
					
				}
					

				//Verificar accion para campañas
				actionCampaing(src, nickname,valPost >= valToComply);
				log.info("===> VALIDACIONES CON LAS QUE NO CUMPLIO LA CAMPAÑA");
				url = url.replace("{{idTweet}}", postid);	
				log.info("==>URL post Not SEND: " + urlPost);
				log.info("Data obtenida del request ===============================>");
				log.info(new Gson().toJson(dataObtenida));
				log.info("Reglas que no se cumplieron ["+rulesFails.replaceAll(" ", ",")+"]");
				if(Strings.isEmpty(src.getChannel())){
					//No hay canal a donde enviar el mensaje, se cancela el proceso
				}else {
					//Se realiza el proceso para ver si se tiene que enviar a un canal
					//Verificación final que se cumplan todas las validaciones
									
					if (valPost >= valToComply) {					
						//Envío del Tweet al canal		
						//campaign.user.update;
						String msg = utilities.getMessage(created_at, message, url).toString();
						
						SendPostModel itemTW = new SendPostModel();
						itemTW.setText(msg);
						itemTW.setTo(channelClient);
						utilities.sendPostTweet(itemTW);
						
						result.setMessage("OK");
						result.setCode(200);
						//return result;
					}else {
						result.setMessage("NOT-SEND");
						result.setCode(202);
						log.info(" LA BUSQUEDA No cumple con todas las validasiones");
						log.info("==>URL post Not SEND: " + url);
						log.info("Reglas que no se cumplieron ["+rulesFails.replaceAll(" ", ",")+"]");
					}
				}
			}
		
		} catch (Exception e) {
			
			log.error("###ERROR AL PROCESAR EL LOG__");
			log.error(e.getMessage());
			result.setMessage("Error");
			result.setCode(500);
			//return result;
		}
	}
	
	
	public SendPostTWResponse validatePostCampaign(SendPostTWRequest request) {
		log.info("#####################__validatePostCampaign__#####################");
		Gson gson = new Gson();
		log.info(gson.toJson(request));
		SendPostTWResponse result = new SendPostTWResponse();
		
		try {
			String pathTW = "https://twitter.com/";
			String authorid = "";
			String idioma = "";
			String postid = "";
			String message = "";
			String typeTweet = "original";
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
			
			String created_at ="";
			String text="";
			String url="https://twitter.com/{{username}}/status/{{idTweet}}";
			
			String nameCampaign = "";
			String urlFileCampaign ="https://docs.google.com/spreadsheets/d/{{urlfile}}/edit";
			
			//Tercer objeto matching_rules
			for (Object entry : request.getMatching_rules()) {
				HashMap<String, Object> itemMR = new HashMap<String, Object>();
				itemMR = (HashMap<String, Object>) entry;
				//themeAndChannel = itemMR.get("tag").toString();
				nameCampaign = itemMR.get("tag").toString().split(":")[0];
				urlFileCampaign = urlFileCampaign.replace("{{urlfile}}", itemMR.get("tag").toString().split(":")[1]);
				System.out.print("=> Client and Channel: " + itemMR.get("tag"));
			}
			
			
			return result;
		} catch (Exception e) {
			log.error("###_ERROR___validatePostCampaign__###");
			log.error(e.getMessage());
			
			result.setCode(500);
			result.setMessage("ERROR: "+e.getMessage() );
			return result;
		}
	}
	

	private void actionCampaing(Searches src, String nickname, boolean fullRules ) {
		log.info("############ actionCampaing");
		try {
			log.info(new Gson().toJson(src));
			log.info(nickname);
			log.info(fullRules);
		
				if(Strings.isNotEmpty(src.getSheet())) {
					String user = "";
					String start = "";
					if(src.getUsers().size() > 0) {
						start = src.getUsers().get(0).substring(0, 1);
					}
					
					for (String u : src.getUsers()) {
						if(u.toLowerCase().equals(nickname.toLowerCase())
								|| u.toLowerCase().equals( "@"+nickname.toLowerCase())
								||u.toLowerCase().equals( nickname.replace("@", "").toLowerCase())) {
							
							user = u;
							break;
						}
					}
					
					Map<String,String> rq = new HashMap<String, String>();
					rq.put("sheet", src.getSheet());
					if(fullRules) {
						log.info("Reglas cumplidas");
						//Posible nuevo usuario
						if(user == "") {
							//nuevo usuario
							log.info("Es un usuario nuevo");
							rq.put("type", "add");
							rq.put("account", start+nickname);
						}else {
							log.info("Actualizando usuario");
							rq.put("type", "update");
							rq.put("account", user);
						}
						
						utilities.post(URL_CAMPAING_USER_UPDATE, new Gson().toJson(rq));
						
					}else {
						if(user != "") { 
							log.info("Actualizando usuario");
							rq.put("type", "update");
							rq.put("account", user);
							utilities.post(URL_CAMPAING_USER_UPDATE, new Gson().toJson(rq));
						}else {
							log.info("Es un usuario nuevo pero no cumplio con las reglas!!!");
						}
					}
				}		
			
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("Error en actionCampaing");
			log.error(e);
		}
	}
	
}

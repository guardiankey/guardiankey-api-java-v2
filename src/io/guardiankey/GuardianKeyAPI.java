package io.guardiankey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GuardianKeyAPI {

	private String authGroupID = "";
	private String APIURI="https://api.guardiankey.io";
	private String key;
	private String iv;
	private String orgId ="";
	private String service ="MyService";
	private String agentId ="";
	private Boolean reverse = new Boolean(false);

	public void setConfig(Map<String,String> config) {
		if(config==null)
			return;
		
		if(config.get("guardiankey.authgroupid")!=null)
			this.authGroupID = config.get("guardiankey.authgroupid");
		if(config.get("guardiankey.apiurl")!=null)
			this.APIURI      = config.get("guardiankey.apiurl");
		if(config.get("guardiankey.orgid")!=null)
			this.orgId 		 = config.get("guardiankey.orgid");
		if(config.get("guardiankey.service")!=null)
			this.service 	 = config.get("guardiankey.service");
		if(config.get("guardiankey.agentid")!=null)
			this.agentId     = config.get("guardiankey.agentid");
		if(config.get("guardiankey.reverse")!=null)
			this.reverse     = (config.get("guardiankey.reverse").contentEquals("true"))? new Boolean(true) : new Boolean(false) ;
		if(config.get("guardiankey.key")!=null)
			this.key         = config.get("guardiankey.key");
		if(config.get("guardiankey.iv")!=null)
			this.iv          = config.get("guardiankey.iv");
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> postMsg(HttpClient HTTPclient, String URI, String msg) {
		try {
			HttpPost post = new HttpPost(URI);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "text/plain");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			// To compute hash
			msg = msg.replaceAll("\"","\\\"");
            byte[] encodedhash = digest.digest((msg+this.key+this.iv).getBytes(StandardCharsets.UTF_8));
            String hashmac = this.bytesToHex(encodedhash);
			// To create POST string
			msg = msg.replaceAll("\\\"","\\\\\"");
		    StringEntity params =new StringEntity("{\"id\":\""+this.authGroupID+"\",\"message\":\""+msg+"\",\"hash\":\""+hashmac+"\"} ");
			post.setEntity(params);
			HttpResponse response = HTTPclient.execute(post);
			if(response.getStatusLine().getStatusCode()!=200)
				return null;
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);			
			Map<String,String> map = new HashMap<String,String>();
		    Gson gson = new GsonBuilder().create();
	        return gson.fromJson(json, map.getClass());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	  private Map<String,String> postMsgTimeout(HttpClient client, String URI, String msg) {

		  Callable<Map<String,String>> taskToSubmit = new Callable<Map<String,String>>() {
			  @Override
			  public Map<String,String> call() {
				  return postMsg(client,  URI,  msg);
			  }
		  };
		  ExecutorService executor = Executors.newSingleThreadExecutor();
		  Future<Map<String,String>> future = executor.submit(taskToSubmit);
		  executor.shutdown(); // This does not cancel the already-scheduled task.
		  Map<String,String> o=null;
		  try {
			  o= future.get(5, TimeUnit.SECONDS);
		  } catch (Exception e) {	}

		  if (!executor.isTerminated())
			  executor.shutdownNow();

		  return o;
	}
	
	public String createMsg(String username, String email, boolean loginFailed, String eventType, String clientIP, String userAgent) {
		
		Long genTime = System.currentTimeMillis()/1000;
		
		Map<String,String> sjson = new HashMap<String,String>();
		sjson.put("generatedTime",genTime.toString());
		sjson.put("agentId",agentId);
		sjson.put("organizationId",orgId);
		sjson.put("authGroupId",authGroupID);
		sjson.put("service",service);
		sjson.put("clientIP",clientIP);
		sjson.put("clientReverse","");
		sjson.put("userName",username);
		sjson.put("authMethod","");
		sjson.put("loginFailed",(loginFailed)? "1" : "0");
		sjson.put("userAgent",userAgent);
		sjson.put("psychometricTyped","");
		sjson.put("psychometricImage","");
		sjson.put("event_type",eventType);
		sjson.put("userEmail",email);
		
        Gson gson = new GsonBuilder().create();
        String txtMsg = gson.toJson(sjson);
        
		return txtMsg;
	}
		
	public Map<String,String> checkAccess(HttpClient client, String username, String email, boolean loginFailed, String eventType, String clientIP, String userAgent) {

		HashMap<String,String> returnObj = new HashMap<String,String>();
		String msg=createMsg(username,email,loginFailed,eventType,clientIP,userAgent);
		String uri = this.APIURI+"/v2/checkaccess";
		Map<String,String> o = postMsgTimeout( client,  uri,  msg);
		
		if(o==null) {
			returnObj.put("response", "ERROR");
		}else {
			if(o.containsKey("response"))
				return o;
			else {
				returnObj.put("response", "ERROR");
				return returnObj;
			}
		}
		return returnObj;
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
    }
	
}

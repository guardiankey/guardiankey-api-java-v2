import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import io.guardiankey.GuardianKeyAPI;

public class Example {
	
	public static void main(String[] args) {

		HttpClient client = HttpClientBuilder.create().build();
		Map<String,String> config = new HashMap<String,String>();
		
		/*
		 * Take the information below in the "Deployment" tab of your 
		 * AuthGroup, in the GuardianKey's Administration Panel.
		 */
		config.put("guardiankey.orgid", "");
		config.put("guardiankey.authgroupid", "");
		config.put("guardiankey.key", "");
		config.put("guardiankey.iv", "");
		
		// Put some information to identify your service
		config.put("guardiankey.service", "MyJavaSite");
		
		// Put a hashid or an information to identify the server or system, generator of events
		config.put("guardiankey.agentid", "server001");
		
		// Leave "https://api.guardiankey.io" for the cloud service
		config.put("guardiankey.apiurl", "https://api.guardiankey.io");
		
		// It is useful to have reverse DNS enabled
		config.put("guardiankey.reverse", "false");
		
		GuardianKeyAPI GK = new GuardianKeyAPI();
		
		// Set configuration
		GK.setConfig(config);
		
		// This is an example, you should use it to send events
		String username="test";
		String email="mail@test.com"; /* The user's e-mail. Its is required only if you want GuardianKey to notify users via SMTP */
		Boolean loginFailed = false;
		String eventType = "Authentication"; /* GK supports Authentication and Registration events */
		String clientIP = "1.1.1.1";
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
		
		// Submit the event for the GuardianKey engine
		Map<String,String> GKRet = GK.checkAccess(client, username, email, loginFailed, eventType, clientIP, userAgent);
		
		// Print the event, just to see if it's OK. You may not need it in production!
		for (Iterator<String> iterator = GKRet.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			System.out.print(key+" -> "+GKRet.get(key)+"\n");
		}
		
		// Evaluate the response and block if GK recommend it
		if(GKRet!=null && GKRet.containsKey("response") && GKRet.get("response").equals("BLOCK") )
		{
			// Implement your blocking code here
		}
	}
}

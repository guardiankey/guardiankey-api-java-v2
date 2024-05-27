
import io.guardiankey.GuardianKeyAPI;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Test2 {
	
	public static void main(String[] args) {
        System.out.print(Test2.checkGuardianKey("test", "", "164.41.38.1", "", false));

    }



   protected static String checkGuardianKey(String username, String email, String clientIP,String userAgent, boolean loginFailed) {
    try {
        HttpClient client = HttpClientBuilder.create().build();
        Map<String,String> config = new HashMap<String,String>();
        config.put("guardiankey.orgid", "");
        config.put("guardiankey.authgroupid", "");
        config.put("guardiankey.key", "");
        config.put("guardiankey.iv", "");
        config.put("guardiankey.service", "");
        config.put("guardiankey.agentid", "");
        config.put("guardiankey.apiurl", "https://api.guardiankey.io");
        GuardianKeyAPI GK = new GuardianKeyAPI();
        GK.setConfig(config);
        String eventType = "Authentication";
        Map<String,String> GKRet = GK.checkAccess(client, username, email, loginFailed, eventType, clientIP, userAgent);        
        // Print the event, just to see if it's OK. You may not need it in production!
        // for (Iterator<String> iterator = GKRet.keySet().iterator(); iterator.hasNext();) {
        //     String key = (String) iterator.next();
        //     System.out.print(key+" -> "+GKRet.get(key)+"\n");
        // }
        
        if(GKRet!=null && GKRet.containsKey("response")) {
            return GKRet.get("response");
        } else { return "ERROR"; }
    } catch (Exception e) {
         return "ERROR";
    }
    
}

	
}


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
        config.put("guardiankey.orgid", "5a6c53e5d1224646d20a0f816891cab28ee94964323d7065b15e9e82e5ebfce0");
        config.put("guardiankey.authgroupid", "d1ed02fa7f9ffacf6c7da5573c8f127584b0583206cd4994e6b887d4502d92f8");
        config.put("guardiankey.key", "FBfFpm1e48s5lUM+pP34Yu38RwEuoud68JMZ2stLK9Q=");
        config.put("guardiankey.iv", "23tiva4C7RioiHeyRmx3HA==");
        config.put("guardiankey.service", "PortalCidadao");
        config.put("guardiankey.agentid", "backendserver");
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
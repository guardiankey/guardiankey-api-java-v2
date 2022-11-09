
import io.guardiankey.GuardianKeyAPI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Test {
	
	public static void main(String[] args) {
        try {
            String msg = "{\"agentId\":\"server001\",\"loginFailed\":\"0\",\"userAgent\":\"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\",\"clientReverse\":\"\",\"userName\":\"test\",\"psychometricTyped\":\"\",\"organizationId\":\"161d5470619ab52c61c1ed212dd52c1ca0e8a219b6d9d413c44cb9f90dae70cc\",\"generatedTime\":\"1645734760\",\"event_type\":\"Authentication\",\"service\":\"MyJavaSite\",\"clientIP\":\"1.1.1.1\",\"authGroupId\":\"1554b454d09f9c36175ce825227adc5b41580733e5ff6a55f2fc58ac6236af69\",\"userEmail\":\"mail@test.com\",\"authMethod\":\"\",\"psychometricImage\":\"\"}CFNn6hMvvtTefubmMvQVpstE1iJAgdI07MoXVTmPUI4=JSR5E6Leuv3gX12dbQbdcw==";
            System.out.print(msg);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(msg.getBytes(StandardCharsets.UTF_8));
            String hashmac = Test.bytesToHex(encodedhash);
            System.out.print(hashmac+"\n");
        }catch (Exception e) {
        }
        System.out.print("Test\n");
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
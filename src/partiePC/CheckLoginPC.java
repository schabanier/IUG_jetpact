package partiePC;
import java.net.*;

import org.json.simple.*;


public class CheckLoginPC {

	String host = "localhost:8080";
	String username = "admin@programmerguru.com";
	String password = "password";
	
	
	public static void Login(String host, String username, String password) throws Exception {
		
		URL registerURL = new URL("http://"+host+"/useraccount/register/doregister?username="+username+"&password="+password);

		String reponse = HTTPLoader.getTextFile(registerURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg" + obj.get("error_msg"));
	}

}


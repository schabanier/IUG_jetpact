package partiePC;
import java.net.*;

import org.json.simple.*;


public class CheckRegisterPC {

	String host = "localhost:8080";
	String name = "Admin";
	String username = "admin@programmerguru.com";
	String password = "password";
	
	
	public static void Register(String host, String name, String username, String password) throws Exception {
		
		URL registerURL = new URL("http://"+host+"/useraccount/register/doregister?name="+name+"&username="+username+"&password="+password);

		String reponse = HTTPLoader.getTextFile(registerURL);

		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg" + obj.get("error_msg"));
	}

}

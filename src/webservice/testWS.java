package webservice;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.* ;

public class testWS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String reponse = "{ \"tag\": \"register\", \"status\": \"OK\" }";
		
		JSONObject obj = (JSONObject) JSONValue.parse(reponse);
		
		//voir avec philippe retour serveur
		System.out.println("tag : " + obj.get("tag"));
		System.out.println("status : " + obj.get("status"));
		System.out.println("error_msg : " + obj.get("error_msg"));
	}

}

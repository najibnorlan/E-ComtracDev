package com.ssm.integrasi.ws.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class ClientRestService {

	private HttpClient client;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public String callPost(String restUrl, String jsonData, String authorizationTokenHdr) {
		String webisReturn ="";
        client = new DefaultHttpClient();
//        System.out.println("jsonData : "+jsonData);
		try {
		  HttpPost post = new HttpPost(restUrl); 
		  StringEntity input = new StringEntity(jsonData);
		  
		  input.setContentType("application/json");
		  post.setEntity(input);
		  
		  //set authorization
		  if(StringUtils.isNotBlank(authorizationTokenHdr)) {
			  post.setHeader("Authorization", authorizationTokenHdr);
		  }
		  
		  HttpResponse response = client.execute(post);
		  
		  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
			  webisReturn = line;
		  }

		} catch (Exception e) {
			System.out.println("Error in ClientRestService : "+e.toString());
			webisReturn = e.toString();
		}finally {
			client.getConnectionManager().shutdown();
		}
		
	return webisReturn;
	}
	
	public String callGet(String restUrl, String jsonData, String authorizationTokenHdr) {
		String webisReturn ="";
        client = new DefaultHttpClient();
        System.out.println("jsonData : "+jsonData);
		try {
		  HttpGet get = new HttpGet(restUrl); 
//		  StringEntity input = new StringEntity(jsonData);
		  
//		  input.setContentType("application/json");
//		  get.setEntity(input);
		  
		  //set authorization
		  if(StringUtils.isNotBlank(authorizationTokenHdr)) {
			  get.setHeader("Authorization", authorizationTokenHdr);
		  }
		  
		  HttpResponse response = client.execute(get);
		  
		  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
			  webisReturn = line;
		  }

		} catch (Exception e) {
			System.out.println("Error in ClientRestService : "+e.toString());
			webisReturn = e.toString();
		}finally {
			client.getConnectionManager().shutdown();
		}
		
	return webisReturn;
	}
}

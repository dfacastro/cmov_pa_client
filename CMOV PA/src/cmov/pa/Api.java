package cmov.pa;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

public class Api extends Application{
	
	String cookie;
	String IP = "http://95.92.17.205:3000";
	//String IP = "http://172.30.94.186:3000";
	public static User user = new User();
	
	
	
	
	
	
	public int login(String username, String password){
		
		if(username.length() == 0 || password.length() == 0){
			return -1;
		}
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		
		HttpResponse response=null;
        try {
        	
            String url = IP + "/user/login?username=" + username + "&password=" + password; 
            
            System.out.println(url);

            HttpGet httpget = new HttpGet(url);
            
            httpget.setHeader("Accept", "application/json");
            
            response = httpClient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	cookie = response.getFirstHeader("Set-Cookie").getValue().toString();
            	user.setUsername(username);
            	System.out.println(cookie);
            	 return 0;
            	 
            }else{
            	return -2;
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    	return -3;

		
		//return 0;
	}
	
	
	
	public boolean getProfile(){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
        try {
        	
            String url = IP + "/user/profile";       
 
            HttpGet httpget = new HttpGet(url);
            
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Cookie", cookie);
            
            response = httpClient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	
            	
                InputStream instream = response.getEntity().getContent();
                String tmp = read(instream);
                
            	
    	        JSONObject messageReceived = new JSONObject(tmp.toString());
            	System.out.println(messageReceived.toString());

            	
            	//guardar os dados comuns
            	user.setDoctor(messageReceived.get("utilizador_type").toString());
            	
            	user.setBirthDate(messageReceived.get("birthdate").toString());
            	user.setName(messageReceived.get("name").toString());
    	        JSONObject utilizadorInfo =messageReceived.getJSONObject("utilizador");

            	
            	System.out.println(user.isDoctor());
            	
            	//TODO: ver se ta bem
            	if(user.isDoctor()){
            		System.out.println(utilizadorInfo.get("photo").toString());
            		
	            	user.setPhoto(utilizadorInfo.get("photo").toString());
            	}else{
            		user.setAddress(utilizadorInfo.get("address").toString());
            		user.setSex(utilizadorInfo.get("sex").toString());
            	}
            	
            	return true;
            }	
            
        } catch (IOException ex) {
        	ex.printStackTrace();    	
        } catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        
        return false;
		
		/*
		//dados para teste sem server
		user.setAddress("perdi-o");
		user.setName("FERNANDO");
		user.setUsername("fmg");
		user.setDoctor("Doctor");
		user.setPhoto("http://www.ackbar.org/images/pikachu.png");
		user.setBirthDate("1989-09-28");
		
		return true;
		*/
	}
	
	
	public User getPatientProfile(String id){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		
		
		
		User user = new User();
		
		try {
        	
			String url = IP + "/patient/show?patient_id=" + id;
			
			System.out.println(url);
			
            HttpGet httpget = new HttpGet(url);
            
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Cookie", cookie);
            
            response = httpClient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	
            	
                InputStream instream = response.getEntity().getContent();
                String tmp = read(instream);
                
            	
    	        JSONObject messageReceived = new JSONObject(tmp.toString());
            	System.out.println(messageReceived.toString());
            	
            	
    	        JSONObject utilizadorInfo =messageReceived.getJSONObject("user");
       
            	
            	user.setAddress(messageReceived.get("address").toString());
            	user.setSex(messageReceived.get("sex").toString());
            	
            	user.setBirthDate(utilizadorInfo.get("birthdate").toString());
            	user.setName(utilizadorInfo.get("name").toString());
            	user.setDoctor(utilizadorInfo.get("utilizador_type").toString());
            	
            	user.setUsername("");
            	
            	
            	
            	
            	return user;
            }	
            
        } catch (IOException ex) {
        	ex.printStackTrace();    	
        } catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        return null;
	}
	
	
	public int regist(String username, String pass, String nome, String datanasc, String morada, String sexo){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		 
		 HttpResponse response=null;
		 
		 String url = IP + "/user/create";        
		
         HttpPost httpPost = new HttpPost(url);         
         JSONObject jsonuser=new JSONObject();

         try {
        	jsonuser.put("username", username);
        	jsonuser.put("password", pass);
        	jsonuser.put("name", nome);
        	jsonuser.put("birthdate", datanasc);
        	jsonuser.put("address", morada);
        	jsonuser.put("sex", sexo);
             
             
            String POSTText = jsonuser.toString();
            StringEntity entity; 
        	 
			entity = new StringEntity(POSTText, "UTF-8");
			BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
	        httpPost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	        entity.setContentType(basicHeader);
	        httpPost.setEntity(entity);
	        response = httpClient.execute(httpPost);
         
	        
	        if(response.getStatusLine().getStatusCode() == 200){
            	
	        	 System.out.println("aki");
            	
            	 return 0;
            	 
            }else
            	System.out.println("peido");
            	return -1;
	        
         
         } catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
         } catch (ClientProtocolException e) {
			e.printStackTrace();
         } catch (IOException e) {
			e.printStackTrace();
         } catch (JSONException e) {
			e.printStackTrace();
		}
		 
		return -1;
	}
	
	
	
	public boolean logout(){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		
		HttpResponse response=null;
        try {
        	
            String url = IP + "/user/logout";  
            
            
 
            HttpGet httpget = new HttpGet(url);
            
            
            httpget.setHeader("Cookie", cookie);
            httpget.setHeader("Accept", "application/json");
            
            response = httpClient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	System.out.println("fim");
            	cookie = "";     	
            	
            	 return true;
            	 
            }else
            	return false;
            
        } catch (IOException ex) {
        	ex.printStackTrace();
    	
        }
		return false;	
	}
	
	
	
	 private String read(InputStream in) throws IOException {
	        StringBuilder sb = new StringBuilder();
	        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
	        for (String line = r.readLine(); line != null; line = r.readLine()) {
	            sb.append(line);
	        }
	        in.close();
	        return sb.toString();
	    }
	
}

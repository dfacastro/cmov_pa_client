package cmov.pa;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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

import cmov.pa.database.DatabaseAdapter;

import utils.SchedulePlan;
import utils.WeekDay;
import utils.WorkDay;

import android.app.Application;

public class Api extends Application{
	
	public static String cookie;
	//public static final String IP = "http://95.92.200.69:3000";
	public static final String IP = "http://172.30.94.186:3000";
	public static User user = new User();	
	public static DatabaseAdapter dbAdapter;
	
	public Map<String, ArrayList<User>> getDoctorsAndSpecialties(){
		return dbAdapter.getDoctorsAndSpecialties();
	}
	
	public Map<String, User> getPatientAppointments(){	
		return dbAdapter.getPatientAppointments(user.getId());
	}
	
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
    	        
    	        user.setId(utilizadorInfo.getInt("id"));
            	
            	System.out.println(user.isDoctor());
            	
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
	
	
	public boolean updateDBIFNecessary(){
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
       try {
       	
           String url = IP + "/version/show";       

           HttpGet httpget = new HttpGet(url);
           
           httpget.setHeader("Accept", "application/json");
           httpget.setHeader("Cookie", cookie);
           
           response = httpClient.execute(httpget);
           
           if(response.getStatusLine().getStatusCode() == 200){
           	
               InputStream instream = response.getEntity().getContent();
               String tmp = read(instream);
               
           	
   	        	JSONObject messageReceived = new JSONObject(tmp.toString());
   	        	System.out.println(messageReceived.toString());
           	
   	        	
   	        	int serverDoctors_version = messageReceived.getInt("version");	
   	        	System.out.println("Versao medicos do server: " + serverDoctors_version);
   	        	
   	        	dbAdapter.open();
   	        	
   	        	int localDoctors_version = dbAdapter.getDoctorsVersion();
   	        	System.out.println("Versao medicos local: " + localDoctors_version);
   	        	
   	        	if(serverDoctors_version > localDoctors_version){
   	        		
   	        		dbAdapter.dropMedics();
   	        		
   	        		System.out.println("Vai fazer update dos medicos");
	   	        	
	   	        	updateDBDoctors();
   	        	}
   	        	
   	        	if(!user.isDoctor()){
   	        		
   	        		int serverAppointments_version = messageReceived.getInt("patient");
   	        		int localAppointments_version = dbAdapter.getPatientVersion(user.getId());
   	        		
   	        		System.out.println("Versao appointments do server: " + serverAppointments_version);
   	        		System.out.println("Versao appointments local: " + localAppointments_version);
   	        		
   	        		if(serverAppointments_version > localAppointments_version){

   	        			dbAdapter.dropAppointmentsPatient(user.getId());
   	        			
	   	        		System.out.println("Vai fazer update das consultas");
	   	        		
	   	        		updateDBPatientAppointments();
   	        		}
   	        	}
   	        	
   	        	
   	        	dbAdapter.close();
   	        	
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
	}
	
	
 	public boolean updateDBDoctors(){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;	
		
		try {
        	
			String url = IP + "/version/update_db";
			
			System.out.println(url);
			
            HttpGet httpget = new HttpGet(url);
            
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Cookie", cookie);
            
            response = httpClient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){	
            	
                InputStream instream = response.getEntity().getContent();
                String tmp = read(instream);
                
            	
    	        JSONArray messageReceived = new JSONArray(tmp.toString());
            	System.out.println(messageReceived.toString());
            	
            	int version = messageReceived.getInt(0);
            	//adicionada a versao
            	dbAdapter.setDoctorsVersion(version);
            	
            	JSONObject specialtyObject = new JSONObject();
            	
            	JSONArray doctorsArray = new JSONArray();
            	JSONObject doctorObject = new JSONObject();
            
            	JSONArray scheduleArray = new JSONArray();
            	JSONObject scheduleObject = new JSONObject();
            	
            	JSONArray workdayArray = new JSONArray();
            	JSONObject workdayObject = new JSONObject();
            	
            	String specialty_name, 
            		   doctor_name, doctor_birthdate, doctor_photo, doctor_sex,
            		   schedule_start_date;
            	
            	int specialty_id,
            		doctor_id,
            		schedule_id,
            		workday_id, workday_end, workday_start, workday_weekday;
            	
            	boolean schedule_active;
            	
            	for(int i = 1; i< messageReceived.length(); i++){
            		specialtyObject = messageReceived.getJSONObject(i);
      
            		specialty_id = specialtyObject.getInt("id");
            		specialty_name = specialtyObject.getString("name");
            		
            		System.out.println("Especialidade: " + specialty_id + " " + specialty_name);
            		//adicona a BD
            		dbAdapter.createSpecialty(specialty_id, specialty_name);
            		
            		doctorsArray = specialtyObject.getJSONArray("doctors");
            		for(int k = 0; k < doctorsArray.length(); k++){
            			
            			doctorObject = doctorsArray.getJSONObject(k);
            			
            			doctor_id = doctorObject.getInt("id");
            			doctor_photo = doctorObject.getString("photo");
            			doctor_name = doctorObject.getJSONObject("user").getString("name");
            			doctor_birthdate = doctorObject.getJSONObject("user").getString("birthdate");
            			doctor_sex = doctorObject.getString("sex");
            			
            			System.out.println("Medico: " + doctor_id + " " + doctor_name + " " + doctor_birthdate + " " + doctor_sex + " " + doctor_photo);
                		//adicona a BD
            			dbAdapter.createDoctor(doctor_id, specialty_id, doctor_name, doctor_birthdate, doctor_sex, doctor_photo);
            			
            			scheduleArray = doctorObject.getJSONArray("schedule_plans");
            			for(int y = 0; y < scheduleArray.length(); y++){
            				
            				scheduleObject = scheduleArray.getJSONObject(y);
            				
            				schedule_id = scheduleObject.getInt("id");
            				schedule_start_date = scheduleObject.getString("start_date");
            				schedule_active = scheduleObject.getBoolean("active");
            				
            				System.out.println("Schedule: " +schedule_id + " " + schedule_active + " " + schedule_start_date);
            				
                    		//adicona a BD            				
            				dbAdapter.createSchedulePlan(schedule_id, schedule_active, doctor_id, schedule_start_date);
            				
            				workdayArray = scheduleObject.getJSONArray("workdays");
            				for(int z = 0; z < workdayArray.length(); z++){
            					
            					workdayObject = workdayArray.getJSONObject(z);
            					
            					workday_id = workdayObject.getInt("id");
            					workday_end = workdayObject.getInt("end");
            					workday_start = workdayObject.getInt("start");
            					workday_weekday = workdayObject.getInt("weekday");
            					
            					System.out.println("Workday: " + workday_id + " " + workday_start + " " + workday_end + " " + workday_weekday);
            					
                        		//adicona a BD
            					dbAdapter.createWorkDay(workday_id, workday_weekday, workday_start, workday_end, schedule_id);            					
            				}	
            			}            			
            		}	
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
	}
	
	
	public boolean updateDBPatientAppointments(){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;	
		
		try {
       	
			String url = IP + "/appointment/index";
			
			System.out.println(url);
			
           HttpGet httpget = new HttpGet(url);
           
           httpget.setHeader("Accept", "application/json");
           httpget.setHeader("Cookie", cookie);
           
           response = httpClient.execute(httpget);
           
           if(response.getStatusLine().getStatusCode() == 200){	
           	
               	InputStream instream = response.getEntity().getContent();
               	String tmp = read(instream);
		
               	JSONArray messageReceived = new JSONArray(tmp.toString());
           		System.out.println(messageReceived.toString());
           		
           		
           		String schedule_date, schedule_time;
           		int doctor_id, patient_id, appointment_id;
           		
           		int version = messageReceived.getInt(0);
           		//adicionada a versao
           		dbAdapter.setPatientVersion(user.getId(), version);
           		
           		JSONObject appointmentObject = new JSONObject();
           		for(int i = 1; i < messageReceived.length(); i++){
           			
           			appointmentObject = messageReceived.getJSONObject(i);
           			           			
           			doctor_id = appointmentObject.getInt("doctor_id");
           			appointment_id = appointmentObject.getInt("id");
           			patient_id = appointmentObject.getInt("patient_id");
           			schedule_date = appointmentObject.getString("scheduled_date");
           			schedule_time = appointmentObject.getString("scheduled_time");
           			
           			System.out.println(appointment_id + " " +doctor_id + " " + patient_id + " " + schedule_date + " "+ schedule_time);
           			
           			dbAdapter.createPatientAppointment(appointment_id, patient_id, doctor_id, schedule_date, schedule_time);
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
           
           
	}
	
	
	public Map<String, Map<String, User>> getDoctorAppointmentsForDate(String date) throws ClientProtocolException, IOException{	
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		
		Map<String,  Map<String, User>> map= new HashMap<String, Map<String, User>>();
		Map<String, User> submap = new TreeMap<String, User>();
			
		String url = IP + "/doctor/get_appointments?date=" + date;
			
		System.out.println(url);
			
        HttpGet httpget = new HttpGet(url);
           
       //System.out.println(cookie);
           
       httpget.setHeader("Accept", "application/json");
       httpget.setHeader("Cookie", cookie);
       
       response = httpClient.execute(httpget);
       
       if(response.getStatusLine().getStatusCode() == 200){
       	
       	
           InputStream instream = response.getEntity().getContent();
           String tmp = read(instream);
           
       	
	        JSONArray messageReceived;
			try {
				messageReceived = new JSONArray(tmp.toString());
				
				System.out.println(messageReceived.toString());
				String scheduledDate = "0";
				
		       	for(int i = 0; i < messageReceived.length(); i++){
		           	JSONObject messageReceivedIndex = messageReceived.getJSONObject(i);
		           	int patientId = messageReceivedIndex.getInt("patient_id");
		           	scheduledDate = messageReceivedIndex.getString("scheduled_date").toString();
		           	String patientName = ((JSONObject)((JSONObject)messageReceivedIndex.get("patient")).get("user")).getString("name").toString();
		           	String scheduledTime = messageReceivedIndex.getString("scheduled_time").toString();

		           	System.out.println(patientId + " " + scheduledDate + " " + scheduledTime + " " + patientName);
		           	
		           	User u = new User();
		           	u.setName(patientName);
		           	u.setId(patientId);
		           	submap.put(scheduledTime, u);
		       	}
		       	
		       	if(submap.size() > 0)
		       		map.put(scheduledDate, submap);
		       	
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
       	
			return map;
       }	
       
		System.out.println("ERRO a obter appointments");
		return map;
	}
	
	
	public Map<String, Map<String, User>> getDoctorAppointmentsNextBusyDay(String currentdate) throws ClientProtocolException, IOException{	
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		
		Map<String,  Map<String, User>> map= new HashMap<String, Map<String, User>>();
		Map<String, User> submap = new TreeMap<String, User>();
			
		String url = IP + "/doctor/next_busy_day?date=" + currentdate;
			
		System.out.println(url);
			
        HttpGet httpget = new HttpGet(url);
           
       //System.out.println(cookie);
           
       httpget.setHeader("Accept", "application/json");
       httpget.setHeader("Cookie", cookie);
       
       response = httpClient.execute(httpget);
       
       //System.out.println(response.getStatusLine().getStatusCode());
       if(response.getStatusLine().getStatusCode() == 200){
       	
       	
           InputStream instream = response.getEntity().getContent();
           String tmp = read(instream);
           
       	
	        JSONArray messageReceived;
			try {
				messageReceived = new JSONArray(tmp.toString());
				
				System.out.println(messageReceived.toString());
				String scheduledDate = "0";
				
		       	for(int i = 0; i < messageReceived.length(); i++){
		           	JSONObject messageReceivedIndex = messageReceived.getJSONObject(i);
		           	int patientId = messageReceivedIndex.getInt("patient_id");
		           	scheduledDate = messageReceivedIndex.getString("scheduled_date").toString();
		           	String patientName = ((JSONObject)((JSONObject)messageReceivedIndex.get("patient")).get("user")).getString("name").toString();
		           	String scheduledTime = messageReceivedIndex.getString("scheduled_time").toString();

		           	System.out.println(patientId + " " + scheduledDate + " " + scheduledTime + " " + patientName);
		           	
		           	User u = new User();
		           	u.setName(patientName);
		           	u.setId(patientId);
		           	submap.put(scheduledTime, u);
		       	}
		       	
		       	if(submap.size() > 0)
		       		map.put(scheduledDate, submap);
		       	
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
       	
			return map;
       }	
       
		System.out.println("ERRO a obter appointments");
		return map;
	}
	
	
	
	public Map<String, Map<String, User>> getDoctorAppointmentsPreviousBusyDay(String currentdate) throws ClientProtocolException, IOException{	
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		
		Map<String,  Map<String, User>> map= new HashMap<String, Map<String, User>>();
		Map<String, User> submap = new TreeMap<String, User>();
			
		String url = IP + "/doctor/previous_busy_day?date=" + currentdate;
			
		System.out.println(url);
			
        HttpGet httpget = new HttpGet(url);
           
       //System.out.println(cookie);
           
       httpget.setHeader("Accept", "application/json");
       httpget.setHeader("Cookie", cookie);
       
       response = httpClient.execute(httpget);
       
       //System.out.println(response.getStatusLine().getStatusCode());
       if(response.getStatusLine().getStatusCode() == 200){
       	
       	
           InputStream instream = response.getEntity().getContent();
           String tmp = read(instream);
           
       	
	        JSONArray messageReceived;
			try {
				messageReceived = new JSONArray(tmp.toString());
				
				System.out.println(messageReceived.toString());
				String scheduledDate = "0";
				
		       	for(int i = 0; i < messageReceived.length(); i++){
		           	JSONObject messageReceivedIndex = messageReceived.getJSONObject(i);
		           	int patientId = messageReceivedIndex.getInt("patient_id");
		           	scheduledDate = messageReceivedIndex.getString("scheduled_date").toString();
		           	String patientName = ((JSONObject)((JSONObject)messageReceivedIndex.get("patient")).get("user")).getString("name").toString();
		           	String scheduledTime = messageReceivedIndex.getString("scheduled_time").toString();

		           	System.out.println(patientId + " " + scheduledDate + " " + scheduledTime + " " + patientName);
		           	
		           	User u = new User();
		           	u.setName(patientName);
		           	u.setId(patientId);
		           	submap.put(scheduledTime, u);
		       	}
		       	
		       	if(submap.size() > 0)
		       		map.put(scheduledDate, submap);
		       	
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
       	
			return map;
       }	
       
		System.out.println("ERRO a obter appointments");
		return map;
	}

	
	public ArrayList<String> getDoctorBusyDays() throws IOException{
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		
		ArrayList<String> busyDays = new ArrayList<String>();
			
		String url = IP + "/doctor/busy_days";
			
		System.out.println(url);
			
       HttpGet httpget = new HttpGet(url);
          
      //System.out.println(cookie);
          
      httpget.setHeader("Accept", "application/json");
      httpget.setHeader("Cookie", cookie);
      
      response = httpClient.execute(httpget);
      
      if(response.getStatusLine().getStatusCode() == 200){
      	
      	
          InputStream instream = response.getEntity().getContent();
          String tmp = read(instream);
          
      	
	        JSONArray messageReceived;
			try {
				messageReceived = new JSONArray(tmp.toString());
				
				System.out.println(messageReceived.toString());
				
				
		       	for(int i = 0; i < messageReceived.length(); i++){
		       		busyDays.add(messageReceived.get(i).toString());
		       	}
		       	
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
      	
			return busyDays;
      }	
      
		System.out.println("ERRO a obter busydays");
		return busyDays;
		
		
	}
	
	
	public String regist(String username, String pass, String nome, String datanasc, String morada, String sexo){
		
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
         
	           
            JSONArray json_errors = new JSONArray(read(response.getEntity().getContent()));
            String errors = "";
            
            for(int i = 0; i < json_errors.length(); i++)
            	errors += json_errors.getString(i) + "\n"; 
            
            return errors;
	        
         
         } catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "Unsupported Encoding Exception";
         } catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Client Protocol Exception";
         } catch (IOException e) {
			e.printStackTrace();
			return "IO Exception";
         } catch (JSONException e) {
			e.printStackTrace();
			return "JSON  Exception";
		}
		 
		//return -1;
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
	
	
	
	public int cancelAppointment(int appointment_id){
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		
		HttpResponse response=null;
        try {
        	
            String url = IP + "/appointment/destroy?id=" + appointment_id;  
            
    		System.out.println(url);

 
            HttpDelete httpdel = new HttpDelete(url);
            
            
            httpdel.setHeader("Cookie", cookie);
            httpdel.setHeader("Accept", "application/json");
            
            response = httpClient.execute(httpdel);
            
            System.out.println(response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() == 200){
            	
        		System.out.println("sucesso");
        		return 0;
            	 
            }else if(response.getStatusLine().getStatusCode() == 401){
            	System.out.println("falha: faltam menos de 24h");
            	return -1;
            }else
            	System.out.println("falha: erro a apagar appointment");
            	return -2;
        } catch (IOException ex) {
        	ex.printStackTrace();
    	
        }
		return -2;	
	}
	
	
	public String createSchedule(SchedulePlan sch) {
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		
		HttpResponse response=null;
        try {
        	
            String url = IP + "/schedule_plan/create";  
            
            JSONObject ob = new JSONObject();
            JSONArray days = new JSONArray();
            
            for(int i = 0; i < sch.workdays.size(); i++) {
            	JSONObject day = new JSONObject();
            	day.put("weekday", sch.workdays.get(i).wday.ordinal());
            	day.put("start", sch.workdays.get(i).start);
            	day.put("end", sch.workdays.get(i).end);
            	
            	days.put(day);
            }
            
            //ob.put("doctor_id", user.getId());
            ob.put("days", days);
            ob.put("start_date", sch.start_date);
             
            HttpPost httppost = new HttpPost(url);
            
            
            httppost.setHeader("Cookie", cookie);
            httppost.setHeader("Accept", "application/json");
            
            String POSTText = ob.toString();
            System.out.println("post:" + POSTText);
            System.out.println("id: " + user.getId());
            StringEntity entity; 
        	 
			entity = new StringEntity(POSTText, "UTF-8");
			BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
			httppost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	        entity.setContentType(basicHeader);
	        httppost.setEntity(entity);

            
            response = httpClient.execute(httppost);
            
            JSONArray json_errors = new JSONArray(read(response.getEntity().getContent()));
            String errors = "";
            
            for(int i = 0; i < json_errors.length(); i++)
            	errors += json_errors.getString(i) + "\n"; 
            
            return errors;
            
        } catch (IOException ex) {
        	ex.printStackTrace();
        	return "IO Exception occurred.";
    	
        } catch (JSONException e) {
			e.printStackTrace();
			return "JSON Exception occurred.";
		}
	}
	
	
	public boolean updateSchedulePlans() {
		final HttpClient httpClient =  new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		try {

			String url = IP + "/schedule_plan/index";       

			HttpGet httpget = new HttpGet(url);

			httpget.setHeader("Accept", "application/json");
			httpget.setHeader("Cookie", cookie);

			response = httpClient.execute(httpget);

			if(response.getStatusLine().getStatusCode() == 200){

				InputStream instream = response.getEntity().getContent();
				String tmp = read(instream);


				JSONArray json = new JSONArray(tmp.toString());
				
				user.schs = new Vector<SchedulePlan>();

				for(int i = 0; i < json.length(); i++) {
					JSONObject json_sch = json.getJSONObject(i);
					
					SchedulePlan sch = new SchedulePlan();
					sch.start_date = json_sch.getString("start_date");
					sch.active = json_sch.getBoolean("active");
					sch.id = json_sch.getInt("id");
					
					/**
					 * TODO: workdays
					 */
					
					JSONArray workdays = json_sch.getJSONArray("workdays");
					
					for(int j = 0; j < workdays.length(); j++) {
						WorkDay wd = new WorkDay();
						JSONObject json_wd = workdays.getJSONObject(j);
						
						wd.start = json_wd.getInt("start");
						wd.end = json_wd.getInt("end");
						wd.wday = WeekDay.values()[json_wd.getInt("weekday")];
						
						sch.add(wd);
					}
					
					user.schs.add(sch);
					
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
	}
	
	public String next_available_day(ArrayList<String> hours, int doctor_id, String current_date) {
		final HttpClient httpClient =  new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		try {

			String url = IP + "/schedule_plan/next?doctor_id=" + doctor_id;
			
			if(! current_date.equals(""))
				url += "&date=" + current_date;

			HttpGet httpget = new HttpGet(url);

			httpget.setHeader("Accept", "application/json");
			httpget.setHeader("Cookie", cookie);

			response = httpClient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode() == 200){

				InputStream instream = response.getEntity().getContent();
				String tmp = read(instream);


				JSONObject json = new JSONObject(tmp.toString());
				
				JSONArray jHours = json.getJSONArray("hours");
				
				//hours.removeAllElements();
				hours.clear();
				for(int i = 0; i < jHours.length(); i++)
					hours.add(jHours.getString(i));
				
				return json.getString("date");
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();   
			return "IO Exception";
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "Illegal State Exception";
		} catch (JSONException e) {
			e.printStackTrace();
			return "JSON Exception";
		}
			
		return "An error occurred";
	}
	
	public String previous_available_day(ArrayList<String> hours, int doctor_id, String current_date) {
		final HttpClient httpClient =  new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		HttpResponse response=null;
		try {

			String url = IP + "/schedule_plan/previous?doctor_id=" + doctor_id + "&date=" + current_date;

			HttpGet httpget = new HttpGet(url);

			httpget.setHeader("Accept", "application/json");
			httpget.setHeader("Cookie", cookie);

			response = httpClient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode() == 200){

				InputStream instream = response.getEntity().getContent();
				String tmp = read(instream);


				JSONObject json = new JSONObject(tmp.toString());
				
				JSONArray jHours = json.getJSONArray("hours");
				
				//hours.removeAllElements();
				hours.clear();
				for(int i = 0; i < jHours.length(); i++)
					hours.add(jHours.getString(i));
				
				return json.getString("date");
			}
			else if(response.getStatusLine().getStatusCode() == 404){
				return null;
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();   
			return "IO Exception";
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "Illegal State Exception";
		} catch (JSONException e) {
			e.printStackTrace();
			return "JSON Exception";
		}
			
		return "An error occurred";
	}
	
	public String createAppointment(int id, String date) {
		
		final HttpClient httpClient =  new DefaultHttpClient();
		 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
		
		HttpResponse response=null;
        try {
        	
            String url = IP + "/appointment/create";  
            
            JSONObject ob = new JSONObject();
            
            ob.put("doctor_id", id);
            ob.put("date", date);
            
             
            HttpPost httppost = new HttpPost(url);
            
            
            httppost.setHeader("Cookie", cookie);
            httppost.setHeader("Accept", "application/json");
            
            String POSTText = ob.toString();
            System.out.println("post:" + POSTText);
            System.out.println("id: " + user.getId());
            StringEntity entity; 
        	 
			entity = new StringEntity(POSTText, "UTF-8");
			BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
			httppost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	        entity.setContentType(basicHeader);
	        httppost.setEntity(entity);

            
            response = httpClient.execute(httppost);
            
            switch(response.getStatusLine().getStatusCode()) {
            
            	case 200:
            		return "";
            		
            	case 500:
		            
		            JSONArray json_errors = new JSONArray(read(response.getEntity().getContent()));
		            String errors = "";
		            
		            for(int i = 0; i < json_errors.length(); i++)
		            	errors += json_errors.getString(i) + "\n"; 
		            
		            return errors;
		            
            	case 401:
            		return "Appointment doesn't exist.";
            }
            
        } catch (IOException ex) {
        	ex.printStackTrace();
        	return "IO Exception occurred.";
    	
        } catch (JSONException e) {
			e.printStackTrace();
			return "JSON Exception occurred.";
		}
        
        return "An error occurred.";
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

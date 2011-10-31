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

import cmov.pa.database.DatabaseAdapter;

import utils.SchedulePlan;
import utils.WeekDay;
import utils.WorkDay;

import android.app.Application;

public class Api extends Application{
	
	public static String cookie;
	public static final String IP = "http://95.92.200.69:3000";
	//String IP = "http://172.30.94.186:3000";
	public static User user = new User();
	
	public static DatabaseAdapter dbAdapter;
	
	
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
	
	
	
	public boolean updateDB(){
		
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
            	
            	int versao = messageReceived.getInt(0);
            	System.out.println("versao-> " + versao);
            	
            	
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
	
	
	public boolean getPatientAppointments(){
		
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
           		
           		
           		String schedule_date, schedule_hour;
           		int doctor_id, patient_id, appointment_id;
           		
           		JSONObject appointmentObject = new JSONObject();
           		for(int i = 0; i < messageReceived.length(); i++){
           			
           			appointmentObject = messageReceived.getJSONObject(i);
           			
           			//TODO: falta a data e a hora
           			
           			doctor_id = appointmentObject.getInt("doctor_id");
           			appointment_id = appointmentObject.getInt("id");
           			patient_id = appointmentObject.getInt("patient_id");
           			
           			System.out.println(appointment_id + " " +doctor_id + " " + patient_id);
           			//TODO: inserir na tabela
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
		           	String patientId = messageReceivedIndex.getString("patient_id").toString();
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
		           	String patientId = messageReceivedIndex.getString("patient_id").toString();
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
		           	String patientId = messageReceivedIndex.getString("patient_id").toString();
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
            }
            
            ob.put("doctor_id", user.getId());
            ob.put("days", days);
             
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

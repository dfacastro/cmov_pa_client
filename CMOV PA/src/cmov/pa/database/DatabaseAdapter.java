package cmov.pa.database;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import cmov.pa.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseAdapter {
	
	private Context context;
	private static SQLiteDatabase database;
	private static DatabaseHelper dbHelper;
	
	
	
	
	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	public DatabaseAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	
	
	/**
	 * Create a new doctor If the doctor is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createDoctor(int id, int specialty_id, String name, String birthdate, String sex, String photo) {
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", id);
		initialvalues.put("specialty_id", specialty_id);
		initialvalues.put("birthdate", birthdate);
		initialvalues.put("sex", sex);
		initialvalues.put("photo", photo);
		initialvalues.put("name", name);

		return database.insert("doctors", null, initialvalues);
	}
	
	
	public long createSpecialty(int specialty_id, String name){
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", specialty_id);
		initialvalues.put("name", name);

		return database.insert("specialties", null, initialvalues);	
	}
	
	
	public long createSchedulePlan(int id, boolean active, int doctor_id, String start_date){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", id);
		initialvalues.put("active", active);
		initialvalues.put("start_date", start_date);
		initialvalues.put("doctor_id", doctor_id);

		return database.insert("schedule_plans", null, initialvalues);		
	}
	
	
	public long createWorkDay(int id, int weekday, int start, int end, int schedule_id){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", id);
		initialvalues.put("weekday", weekday);
		initialvalues.put("start", start);
		initialvalues.put("end", end);
		initialvalues.put("schedule_plan_id", schedule_id);

		return database.insert("workdays", null, initialvalues);		
	}
	

	public long createPatientAppointment(int id, int patient_id, int doctor_id, String schedule_day, String schedule_time){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", id);
		initialvalues.put("patient_id", patient_id);
		initialvalues.put("doctor_id", doctor_id);
		initialvalues.put("scheduled_day", schedule_day);
		initialvalues.put("scheduled_time", schedule_time);

		return database.insert("appointments", null, initialvalues);	
	}
	
	
	public long setDoctorsVersion(int version){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("version", version);

		return database.insert("metadatadoctors", null, initialvalues);	
	}
	

	public long setPatientVersion(int patient_id, int version){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("patient_id", patient_id);
		initialvalues.put("version", version);

		return database.insert("metadatapatient", null, initialvalues);	
	}
	
	
	
	//uma especialidade tem de ter medicos associados
	public Map<String, ArrayList<User>> getDoctorsAndSpecialties(){
		
		Map<String, ArrayList<User>> map = new TreeMap<String, ArrayList<User>>();
		
		open();
		
		String selectSpecialties = "Select * from specialties ";  
	
	 	Cursor specialtiesCursor = database.rawQuery(selectSpecialties, null);
	 	
	 	System.out.println("A construir mapa com os medicos e especialidades");
	 	
	 	specialtiesCursor.moveToFirst();
	 	do {
	 		
	 		int specialty_id = specialtiesCursor.getInt(0);
	 		String specialty_name = specialtiesCursor.getString(1);
	 		
	 		System.out.println("Especialidade: "+ specialty_name + " " +specialty_id);
	 		
	 		String selectDoctors = "Select name, id , photo from doctors where specialty_id = " + specialty_id;
	 		Cursor doctorsCursor = database.rawQuery(selectDoctors, null);
	 		
	 		
	 		ArrayList<User> arr = new ArrayList<User>();
	 		
	 		doctorsCursor.moveToFirst();
		 	do {
		 		
		 		String doctor_name = doctorsCursor.getString(0);
		 		int doctor_id = doctorsCursor.getInt(1);
		 		String doctor_photo = doctorsCursor.getString(2);
		 		
		 		System.out.println(doctor_id + " " + doctor_name + " "+ doctor_photo);
		 		
		 		User u = new User();
		 		u.setId(doctor_id);
		 		u.setName(doctor_name);
		 		u.setPhoto(doctor_photo);
		 		
		 		arr.add(u);
		 		
		 	}while (doctorsCursor.moveToNext());
		 	doctorsCursor.close();
		 	
	 		
	 		map.put(specialty_name, arr);
	 		
	 	}while (specialtiesCursor.moveToNext());
	 	specialtiesCursor.close();
	 	
	 	close();
	 	
	 	return map;
	}
	
	
	public int getDoctorsVersion(){
		
		String selectVersion = "Select * from metadatadoctors ";  
		
	 	Cursor versionCursor = database.rawQuery(selectVersion, null);
	 	
	 	versionCursor.moveToFirst();
	 	
	 	if(versionCursor.getCount() == 0){
	 		versionCursor.close();
	 		return -1;
	 	}
	 	
	 	
	 	int version = versionCursor.getInt(0);
	 	versionCursor.close();
	 	return version;
	}
	
	
	
	public int getPatientVersion(int patient_id){
		
		String selectVersion = "Select * from metadatapatient where patient_id=" + patient_id;  
		
	 	Cursor versionCursor = database.rawQuery(selectVersion, null);
	 	
	 	versionCursor.moveToFirst();
	 	
	 	if(versionCursor.getCount() == 0){
	 		versionCursor.close();
	 		return -1;
	 	}
	 	
	 	int version = versionCursor.getInt(1);
	 	versionCursor.close();
	 	
	 	return version;
	}

	
	
	
	public Map<String, User> getPatientAppointments(int patient_id){
		
		System.out.println("Vai buscar os appointments do user" + patient_id);
		
		Map<String, User> map = new TreeMap<String, User>();
		
		open();
		
		String selectAppointments = "Select * from appointments where patient_id = " + patient_id;
			  
		
	 	Cursor appointmentCursor = database.rawQuery(selectAppointments, null);

	 	if(appointmentCursor.getCount() == 0){
	 		appointmentCursor.close();
	 		return map;
	 	}
	 	
	 	appointmentCursor.moveToFirst();
	 	do {
	 		
	 		
	 		int appointment_id = appointmentCursor.getInt(0);
	 		String scheduled_day = appointmentCursor.getString(3);
	 		String scheduled_time = appointmentCursor.getString(4);
	 		int doctor_id = appointmentCursor.getInt(2);
	 		String doctor_name, doctor_photo;
	 		
	 		String selectDoctors = "Select name , photo from doctors where id = " + doctor_id;
		 	Cursor doctorsCursor = database.rawQuery(selectDoctors, null);
		 	
		 	doctorsCursor.moveToFirst();
		 	do {
		 		
		 		doctor_name = doctorsCursor.getString(0);
		 		doctor_photo = doctorsCursor.getString(1);

		 	}while (doctorsCursor.moveToNext());
		 	doctorsCursor.close();
		 	
	 		User u = new User();
	 		
	 		u.setAssociatedAppointmentId(appointment_id);
	 		u.setName(doctor_name);
	 		u.setId(doctor_id);
	 		u.setPhoto(doctor_photo);
	 		
	 		String key = scheduled_day + " " + scheduled_time;
	 		
	 		System.out.println(key + " " + u.toString());
	 		
	 		map.put(key, u);
	 		
		
	 	}while (appointmentCursor.moveToNext());
	 	appointmentCursor.close();
	 	
	 	close();
	 	
		return map;
	}
	
	
	public void dropMedics(){
		
		//elimina
		
		database.execSQL(" DROP TABLE IF EXISTS metadatadoctors ");
		
		database.execSQL(" DROP TABLE IF EXISTS specialties ");
		database.execSQL(" DROP TABLE IF EXISTS workdays ");
		database.execSQL(" DROP TABLE IF EXISTS schedule_plans "); 
		database.execSQL(" DROP TABLE IF EXISTS doctors ");
		
		
		//cria
		
		database.execSQL("CREATE TABLE specialties (id INTEGER PRIMARY KEY, name TEXT) ");
		database.execSQL(" CREATE TABLE workdays (id INTEGER PRIMARY KEY, weekday INTEGER, start INTEGER, end INTEGER, schedule_plan_id INTEGER) ");
		database.execSQL(" CREATE TABLE schedule_plans (id INTEGER PRIMARY KEY, active BOOLEAN, start_date TEXT, doctor_id INTEGER) ");
		database.execSQL(" CREATE TABLE doctors (name TEXT, birthdate TEXT, id INTEGER PRIMARY KEY, sex TEXT, photo TEXT, specialty_id INTEGER) ");
		database.execSQL("CREATE TABLE metadatadoctors (version INTEGER PRIMARY KEY)");
		
	}
	
	
	public void dropAppointmentsPatient(int patient_id){
		
		String query1 = "delete from appointments where patient_id=" + patient_id;
		String query2 = "delete from metadatapatient where patient_id=" + patient_id;
		
		database.execSQL(query1);
		database.execSQL(query2);
		
	}
	
}

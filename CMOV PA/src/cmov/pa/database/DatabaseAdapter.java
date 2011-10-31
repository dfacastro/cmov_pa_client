package cmov.pa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	
	
	
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
	
	
	
	//workdays (id INTEGER PRIMARY KEY, weekday INTEGER, start INTEGER, end INTEGER, schedule_plan_id INTEGER) ";
	public long createWorkDay(int id, int weekday, int start, int end, int schedule_id){
		
		ContentValues initialvalues = new ContentValues();
		initialvalues.put("id", id);
		initialvalues.put("weekday", weekday);
		initialvalues.put("start", start);
		initialvalues.put("end", end);
		initialvalues.put("schedule_plan_id", schedule_id);

		return database.insert("workdays", null, initialvalues);		
	}
	
	
	

	
	
}

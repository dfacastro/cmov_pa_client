package cmov.pa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "cmovpa.db";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_APPOINTMENTS = " CREATE TABLE appointments (id INTEGER PRIMARY KEY, patient_id INTEGER, doctor_id INTEGER, scheduled_day TEXT, scheduled_time TEXT)";
	private static final String DATABASE_CREATE_SPECIALTIES = " CREATE TABLE specialties (id INTEGER PRIMARY KEY, name TEXT) ";
	private static final String DATABASE_CREATE_WORKDAYS = " CREATE TABLE workdays (id INTEGER PRIMARY KEY, weekday INTEGER, start INTEGER, end INTEGER, schedule_plan_id INTEGER) ";
	private static final String DATABASE_CREATE_SCHEDULE_PLANS = " CREATE TABLE schedule_plans (id INTEGER PRIMARY KEY, active BOOLEAN, start_date TEXT, doctor_id INTEGER) ";
	private static final String DATABASE_CREATE_DOCTORS = " CREATE TABLE doctors (name TEXT, birthdate TEXT, id INTEGER PRIMARY KEY, sex TEXT, photo TEXT, specialty_id INTEGER) ";
	private static final String DATABASE_CREATE_METADATA_PATIENT = "CREATE TABLE metadatapatient (patient_id INTEGER PRIMARY KEY, version INTEGER)";
	private static final String DATABASE_CREATE_METADATA_DOCTORS = "CREATE TABLE metadatadoctors (version INTEGER PRIMARY KEY)";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_APPOINTMENTS);
		database.execSQL(DATABASE_CREATE_SPECIALTIES);
		database.execSQL(DATABASE_CREATE_WORKDAYS);
		database.execSQL(DATABASE_CREATE_SCHEDULE_PLANS);
		database.execSQL(DATABASE_CREATE_DOCTORS);

		database.execSQL(DATABASE_CREATE_METADATA_DOCTORS);
		database.execSQL(DATABASE_CREATE_METADATA_PATIENT);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		
		database.execSQL(" DROP TABLE IF EXISTS appointments "); 
		database.execSQL(" DROP TABLE IF EXISTS specialties ");
		database.execSQL(" DROP TABLE IF EXISTS workdays ");
		database.execSQL(" DROP TABLE IF EXISTS schedule_plans "); 
		database.execSQL(" DROP TABLE IF EXISTS doctors ");
		
		database.execSQL(" DROP TABLE IF EXISTS metadatapatient ");
		database.execSQL(" DROP TABLE IF EXISTS metadatadoctors ");

		
		onCreate(database);
	}
}
package cmov.pa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "CMOV PA";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = 
			"CREATE TABLE appointments (id INTEGER PRIMARY KEY, user_id INTEGER, doctor_id INTEGER, scheduled_date TEXT)" +
			"CREATE TABLE specialties (id INTEGER PRIMARY KEY, name TEXT)" + 
			"CREATE TABLE workdays (id INTEGER PRIMARY KEY, weekday INTEGER, start INTEGER, end INTEGER, schedule_plan_id INTEGER)" +
			"CREATE TABLE schedule_plans (id INTEGER PRIMARY KEY, active BOOLEAN, start_date TEXT, doctor_id INTEGER)" + 
			"CREATE TABLE doctors (name TEXT, birthdate TEXT, _id INTEGER PRIMARY KEY, sex TEXT, photo TEXT, specialty_id INTEGER)";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS appointments" + 
						 "DROP TABLE IF EXISTS specialties" +
						 "DROP TABLE IF EXISTS workdays" + 
						 "DROP TABLE IF EXISTS schedule_plans" +
						 "DROP TABLE IF EXISTS doctors");
		onCreate(database);
	}
}
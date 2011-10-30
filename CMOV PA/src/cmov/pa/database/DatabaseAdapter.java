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
	 * Create a new todo If the todo is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	/*
	public long createTodo(String category, String summary, String description) {
		ContentValues initialvalues = new ContentValues();
		initialvalues.put(KEY_CATEGORY, category);
		initialvalues.put(KEY_SUMMARY, summary);
		initialvalues.put(KEY_DESCRIPTION, description);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}
	*/

	
	
}

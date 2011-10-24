package cmov.pa;

import java.util.ArrayList;
import java.util.Calendar;
import utils.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewSchedule extends Activity {
	
	
	private TextView start_date;
	static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;
    final Calendar c = Calendar.getInstance();
    
    private SchedulePlan sch = new SchedulePlan();
    
	static final String[] COUNTRIES = new String[] {
		    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_schedule);
        
        
        ListView list = (ListView) findViewById(R.id.listView1);
        //ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.schedule_item, COUNTRIES);	
        
        sch.workdays.add(new WorkDay(WeekDay.Monday, 200, 300));
        //ArrayAdapter<WorkDay> a = new ArrayAdapter<WorkDay>(this, R.layout.schedule_item, sch.getArray());	
        WorkDaysAdapter a = new WorkDaysAdapter(this, R.layout.workday_item, new ArrayList<WorkDay>(sch.workdays));
        list.setAdapter(a);
        
        start_date = (TextView) findViewById(R.id.new_schedule_date);
        
        start_date.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(DATE_DIALOG_ID);
					}
				}
        		
        );   
        
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH) + 1;
        updateDisplay();
        
        //Set calendar date to tomorrow
        c.set(Calendar.DAY_OF_MONTH, mDay);
        
        
        //BUTTONS
        Button add = (Button) findViewById(R.id.new_schedule_add_button);
        Button create = (Button) findViewById(R.id.new_schedule_create_button);
        
        add.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(),NewWorkDay.class);
			            startActivityForResult(intent, 0);
					}
				}
        		
        );
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
            switch (id) {

            case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
            }
            return null;
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
            	
            	Calendar chosen_date = Calendar.getInstance();
            	chosen_date.set(Calendar.YEAR, year);
            	chosen_date.set(Calendar.MONTH, monthOfYear);
            	chosen_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            	
            	
            	if (chosen_date.before(c)) {
            		Toast toast = Toast.makeText(getApplicationContext(), "Choose a date after today.", Toast.LENGTH_SHORT);
            		toast.show();
            		return;
            	}
            	
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplay();
            }
    };
    
    private void updateDisplay() {
        start_date.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" "));
    }
    
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {

        case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }    
    
    
}

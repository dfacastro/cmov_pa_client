package cmov.pa;

import utils.WeekDay;
import utils.WorkDay;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewWorkDay extends Activity{
	
	WorkDay workday = new WorkDay();
	int start_mins, end_mins;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_workday);
        
        Spinner weekday = (Spinner) findViewById(R.id.new_workday_weekday_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, WeekDay.toStringArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekday.setAdapter(adapter);   
        
        
        String[] items = new String[] {"00", "30"};
        Spinner start = (Spinner) findViewById(R.id.new_workday_start_spinner);
        Spinner end = (Spinner) findViewById(R.id.new_workday_end_spinner);
        adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start.setAdapter(adapter); 
        end.setAdapter(adapter); 
        
        start.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						start_mins = Integer.parseInt((String) arg0.getItemAtPosition(arg2));
						
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
        			
        		}
        );
        
        end.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						end_mins = Integer.parseInt((String) arg0.getItemAtPosition(arg2));
						
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
        			
        		}
        );
        
        
        
        Button add = (Button) findViewById(R.id.new_workday_add_button);
        add.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

						/**
						 * TODO: validations
						 */

						/**
						 * validacao dos campos
						 */
						
						String start_str = ((EditText) findViewById(R.id.new_workday_start_edit)).getText().toString();
						String end_str = ((EditText) findViewById(R.id.new_workday_end_edit)).getText().toString();
						
						if(start_str.equals("") || end_str.equals("")) {
							Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
							return;
						}
						
						int start_hours = Integer.parseInt(start_str);
						int end_hours = Integer.parseInt(end_str);
						
						if(start_hours > 23 || start_hours < 0 ) {
							Toast.makeText(getApplicationContext(), "Start time must be between 0 and 23", Toast.LENGTH_SHORT).show();
							return;
						}
						if(end_hours > 23 || end_hours < 0 ) {
							Toast.makeText(getApplicationContext(), "End time must be between 0 and 23", Toast.LENGTH_SHORT).show();
							return;
						}
						
						workday.start = start_hours * 60 + start_mins;
						workday.end = end_hours * 60 + end_mins;

						Intent intent = new Intent();
						intent.putExtra("workday", workday);
						setResult(RESULT_OK, intent);
						finish();
						
						
					}
				}
        );
        
        Spinner weekday_spinner = (Spinner) findViewById(R.id.new_workday_weekday_spinner);
        weekday_spinner.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						workday.wday = WeekDay.valueOf((String) arg0.getItemAtPosition(arg2));
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}
		
        		}
        );
        
        
        
    };
	

}

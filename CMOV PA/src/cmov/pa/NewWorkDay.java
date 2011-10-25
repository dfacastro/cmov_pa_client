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
import android.widget.Spinner;
import android.widget.Toast;

public class NewWorkDay extends Activity{
	
	WorkDay workday = new WorkDay();
	WeekDay wday;
	
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
        
        
        Button add = (Button) findViewById(R.id.new_workday_add_button);
        add.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

						/**
						 * TODO: validations
						 */
						
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

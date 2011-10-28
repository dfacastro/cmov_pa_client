package cmov.pa;

import java.util.ArrayList;

import utils.SchedulePlan;
import utils.WorkDay;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MedicSchedule extends Activity {
	private SchedulePlan sch = new SchedulePlan();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        
        Bundle extras = getIntent().getExtras(); 
        
        if(extras != null)
        	sch = (SchedulePlan) extras.getSerializable("schedule");
        
        TextView state = (TextView) findViewById(R.id.schedule_state);
        
        if(sch.active) {
        	state.setText("Active Plan"); 
        	
        	LinearLayout state_layout = (LinearLayout) findViewById(R.id.schedule_date_layout);
        	state_layout.setVisibility(View.INVISIBLE);
        }
        else {
        	state.setText("Future Plan");
        	
        	TextView date = (TextView) findViewById(R.id.schedule_date);
        	date.setText(sch.start_date);
        	
        }
    
        ListView list = (ListView) findViewById(R.id.schedule_workdays_listview);        
        WorkDaysAdapter a = new WorkDaysAdapter(this, R.layout.workday_item, new ArrayList<WorkDay>(sch.workdays));
        list.setAdapter(a);
		
    }

}

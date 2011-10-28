package cmov.pa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MedicScheduleTab extends Activity {
	
	Api api;
	TextView error;
	LinearLayout active_layout;
	LinearLayout future_layout;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		api = ((Api)getApplicationContext());

		setContentView(R.layout.schedule_tab);
		
		error = (TextView) findViewById(R.id.schedule_error);
		active_layout = (LinearLayout) findViewById(R.id.schedule_active_layout);
		future_layout = (LinearLayout) findViewById(R.id.schedule_future_layout);
		
		
		
		updateSchedules();
		
		//TextView t = (TextView) findViewById(R.id.textView1);
		//t.setVisibility(View.INVISIBLE);
		
		
       
		
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (resultCode) {
			case RESULT_OK:
				/**
				 * TODO: update schedules
				 */
				
				updateSchedules();
				break;			
		}
	}
	
	
	private void updateSchedules() {
		api.updateSchedulePlans();
		
		switch(api.user.schs.size()) {
		
			case 0:
				error.setVisibility(View.VISIBLE);
				active_layout.setVisibility(View.INVISIBLE);
				future_layout.setVisibility(View.INVISIBLE);
				break;
				
			case 1:
				error.setVisibility(View.INVISIBLE);
				updateActiveSchedule(View.VISIBLE);
				updateFutureSchedule(View.INVISIBLE);
				
				
				break;
				
			case 2:
				error.setVisibility(View.INVISIBLE);
				updateActiveSchedule(View.VISIBLE);
				updateFutureSchedule(View.VISIBLE);
		
		
		}			
		
	}
	
	private void updateActiveSchedule(int visibility) {
		active_layout.setVisibility(visibility);
		
		if(visibility == View.VISIBLE) {
			TextView working_days = (TextView) findViewById(R.id.schedule_active_working_days);
			working_days.setText("Working days: " + Integer.toString(api.user.getActiveSchedulePlan().workdays.size()));
			
			Button show_active = (Button) findViewById(R.id.schedule_active_button);
			show_active.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					/**
					 * TODO: lançar nova activity com schedule
					 * repetir no updateFuture
					 */
					
					Intent intent = new Intent(getApplicationContext(),MedicSchedule.class);
					intent.putExtra("schedule", api.user.getActiveSchedulePlan());
		            startActivity(intent);
					
				}
			});
			
		}		
		
	}
	
	private void updateFutureSchedule(int visibility) {
		future_layout.setVisibility(visibility);
		
		if(visibility == View.VISIBLE) {
			TextView working_days = (TextView) findViewById(R.id.schedule_future_working_days);
			working_days.setText("Working days: " + Integer.toString(api.user.getFutureSchedulePlan().workdays.size()));
			
			TextView start_date = (TextView) findViewById(R.id.schedule_future_start_date);
			start_date.setText("Start Date: " + api.user.getFutureSchedulePlan().start_date);
			
			Button show_future = (Button) findViewById(R.id.schedule_future_button);
			show_future.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					/**
					 * TODO: lançar nova activity com schedule
					 * repetir no updateFuture
					 */
					
					Intent intent = new Intent(getApplicationContext(),MedicSchedule.class);
					intent.putExtra("schedule", api.user.getFutureSchedulePlan());
		            startActivity(intent);
					
				}
			});
		}
				
		
	}
	
	//

	// Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.medic_schedule_menu, menu);
	    return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.new_schedule:
				Intent intent = new Intent(getApplicationContext(),NewSchedule.class);
	            startActivityForResult(intent, 0);
	            //finish();
	            
		        return true;
		    /*
		    case R.id.help:
		        //showHelp();
				Toast toast = Toast.makeText(getApplicationContext(), "Insira as credenciais ou registe-se", Toast.LENGTH_SHORT);
	    		toast.show();
	    		
		        return true;
		        */
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	@Override
	public void onBackPressed() {
		
		// prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the message to display
        alertbox.setMessage("Prentede sair da aplicacao?");

        // set a positive/yes button and create a listener
        alertbox.setPositiveButton("Nao", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Do nothing
            }
        });

        // set a negative/no button and create a listener
        alertbox.setNegativeButton("Sim", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
            	
            	if(api.logout()){
            		finish();
            	}else{
            		Toast toast = Toast.makeText(getApplicationContext(), "Logout Falhou", Toast.LENGTH_SHORT);
            		toast.show();
            	}
            	
            	
                
            }
        });

        // display box
        alertbox.show();

	}
}

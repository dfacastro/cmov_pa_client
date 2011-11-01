package cmov.pa;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Inicial extends TabActivity{

	Api api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		api = ((Api)getApplicationContext());
		
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ProfileTab.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("profile").setIndicator("Profile",res.getDrawable(R.layout.ic_tab_profile)).setContent(intent);
	    tabHost.addTab(spec);

	    
	    if(api.user.isDoctor()){
		    
		    
		    // Do the same for the other tabs
		    intent = new Intent().setClass(this, MedicAppointmentsTab.class);
		    spec = tabHost.newTabSpec("appointments").setIndicator("Appointments",res.getDrawable(R.layout.ic_tab_appointments)).setContent(intent);
		    tabHost.addTab(spec);
		    
		    
		    intent = new Intent().setClass(this, MedicScheduleTab.class);
		    spec = tabHost.newTabSpec("schedule").setIndicator("Schedule",res.getDrawable(R.layout.ic_tab_schedule)).setContent(intent);
		    tabHost.addTab(spec);
	    }else {
	    	
	    	intent = new Intent().setClass(this, PatientAppointmentsTab.class);
		    spec = tabHost.newTabSpec("appointments").setIndicator("Appointments",res.getDrawable(R.layout.ic_tab_appointments)).setContent(intent);
		    tabHost.addTab(spec);
	    	
	    }

	    tabHost.setCurrentTab(1);
		
	}

}

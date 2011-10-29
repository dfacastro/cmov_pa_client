package cmov.pa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class CreateAppointment extends Activity implements OnDrawerOpenListener, OnDrawerCloseListener{
	
	Api api;
	
	private RelativeLayout relativeLayout;
	private SlidingDrawer slidingDrawer;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		api = ((Api)getApplicationContext());
		
		setContentView(R.layout.create_appointment);		
		
		// Get reference to SlidingDrawer
		slidingDrawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer);

        //Listen for open event
		slidingDrawer.setOnDrawerOpenListener(this);

        // Listen for close event
		slidingDrawer.setOnDrawerCloseListener(this);
		
		relativeLayout = (RelativeLayout)this.findViewById(R.id.relativeLayout_newAppointmentContent);

	}
	
	
	@Override
	public void onDrawerOpened() {
                // Hide listview
		relativeLayout.setVisibility(RelativeLayout.GONE);
	}

	@Override
	public void onDrawerClosed() {
                // now make it visible again
		relativeLayout.setVisibility(RelativeLayout.VISIBLE);	
	}
	
}

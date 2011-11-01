package cmov.pa;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cmov.pa.MedicAppointmentsTab.MyExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PatientAppointmentsTab  extends ExpandableListActivity {
	
	
	Api api;
	ExpandableListAdapter mAdapter;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		api = ((Api)getApplicationContext());

		setContentView(R.layout.patient_appointments_tab);
		
		mAdapter = new MyExpandableListAdapter(this);
	    setListAdapter(mAdapter);
       
        
        
       
    }
	
	// Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.patient_appointment_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.patient_appointments_refresh:
		    	//TODO: fazer...
		        return true;
		    
		    case R.id.patient_new_appointment:
		    	Intent intent = new Intent(getApplicationContext(),CreateAppointment.class);
		        startActivity(intent);
		        return true;
		        
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		
		// prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the message to display
        alertbox.setMessage("Do you want to quit the application?");

        // set a positive/yes button and create a listener
        alertbox.setPositiveButton("No", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Do nothing
            }
        });

        // set a negative/no button and create a listener
        alertbox.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
            	
            	if(api.logout()){
            		finish();
            	}else{
            		Toast toast = Toast.makeText(getApplicationContext(), "Logout failled", Toast.LENGTH_SHORT);
            		toast.show();
            	}
            	
            	
                
            }
        });

        // display box
        alertbox.show();

	}
	
	
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    	
		private Context context;
	
    	@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

    	private String[] groups = new String[0];
    	private ArrayList<User> children = new ArrayList<User>();

        public MyExpandableListAdapter(Context c){
        	
        	context = c;
        	
        	//map de data e con
    		Map<String, User> map = api.getPatientAppointments();

			
			if(map.size() == 0){
				Toast toast = Toast.makeText(getApplicationContext(), "You don't have any appointments", Toast.LENGTH_LONG);
        		toast.show();
        		return;
			}
			
			
			populateAdapter(map);

        
        }
        
        public Object getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(PatientAppointmentsTab.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            	
        	User user = (User) getChild(groupPosition, childPosition);
            if (convertView == null) {
                
            	LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.child_layout, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.child_text);
            tv.setText("   " + user.getName());

            URL newurl;

			Bitmap mIcon_val;
			try {
				
				newurl = new URL(user.getPhoto());
				mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
				((ImageView)convertView.findViewById(R.id.child_image)).setImageBitmap(mIcon_val);
			} catch (IOException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Error downloading image", Toast.LENGTH_SHORT);
	     		toast.show();
			}

           
            return convertView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }
        
        public void reset(){
        	groups = new String[0];
        	children = new ArrayList<User>();
        }
        
        public void populateAdapter(Map <String, User> map){
        	
        	groups = new String[map.size()];
			children = new ArrayList<User>();
        	
        	int i = 0;	
			for(String key: map.keySet()){//este ciclo so vai correr 1 vez...

				groups[i] = key;		
				User u = map.get(key);		
				children.add(u);
				i++;

			}	
        }

    }

}

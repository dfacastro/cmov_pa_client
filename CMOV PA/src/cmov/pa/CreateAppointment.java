package cmov.pa;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import utils.WeekDay;

import cmov.pa.PatientAppointmentsTab.MyExpandableListAdapter;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class CreateAppointment extends ExpandableListActivity implements OnDrawerOpenListener, OnDrawerCloseListener{
	
	Api api;
	
	private RelativeLayout relativeLayout;
	private LinearLayout date_time_layout;
	private SlidingDrawer slidingDrawer;
	ExpandableListAdapter mAdapter;
	
	String date = "";
	String hour = "";
	ArrayList<String> spinner_hours = new ArrayList<String>();
	ArrayAdapter<String> spinner_adapter;
	
	User selectedUser = new User();

    
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
		
		date_time_layout = (LinearLayout) findViewById(R.id.layout_date_and_time);
		date_time_layout.setVisibility(View.INVISIBLE);
		
		
		
		
        Spinner hour_spinner = (Spinner) findViewById(R.id.new_appointment_hour);
        //String arr[] = new String[0];
        spinner_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinner_hours);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour_spinner.setAdapter(spinner_adapter); 
        
        hour_spinner.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						hour = (String) arg0.getItemAtPosition(arg2);						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}
		
        		}
        );
        
        ImageButton forward = (ImageButton) findViewById(R.id.new_appointment_forward);
		forward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				forwardAction();	
			}
		});
		
		ImageButton back = (ImageButton) findViewById(R.id.new_appointment_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
	
				backAction();
	
			}
		});
		
		Button create = (Button) findViewById(R.id.new_appointment_create_button);
		create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String errors = api.createAppointment(selectedUser.getId(), date + " " + hour);
				
				if(errors.equals("")) {
					setResult(RESULT_OK);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), errors, Toast.LENGTH_LONG).show();
					return;
				}
				
			}
		});
        
		
		mAdapter = new MyExpandableListAdapter(this);
	    setListAdapter(mAdapter);
	    
	    Toast toast = Toast.makeText(getApplicationContext(), "Start by Picking a Doctor", Toast.LENGTH_LONG);
 		toast.show();
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
	
	
	
	void forwardAction(){
		
		date = api.next_available_day(spinner_hours, selectedUser.getId(), date );
		
		updateDateAndTime();

		//spinner_adapter.notifyDataSetChanged();
		
		

	}
	
	
	void backAction(){
		
		String result = api.previous_available_day(spinner_hours, selectedUser.getId(), date );
		if (result== null) {
			Toast.makeText(getApplicationContext(), "This is the nearest available day.", Toast.LENGTH_LONG).show();
			return;
		}
		
		date = result;
		updateDateAndTime();
		
		//spinner_adapter.notifyDataSetChanged();
	}
	
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		selectedUser = (User)mAdapter.getChild(groupPosition, childPosition);
		
		System.out.println(selectedUser.getName());
	
		URL newurl;

		Bitmap mIcon_val;
		try {
			
			newurl = new URL(selectedUser.getPhoto());
			mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
			((ImageView)findViewById(R.id.createAppointmentImage)).setImageBitmap(mIcon_val);
		} catch (IOException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "Error downloading image", Toast.LENGTH_SHORT);
     		toast.show();
		}
		
		TextView name = (TextView) findViewById(R.id.new_appointment_medics_name);
		name.setText(selectedUser.getName());
		
		date_time_layout.setVisibility(View.VISIBLE);
		
		date = api.next_available_day(spinner_hours, selectedUser.getId(), "" );
		
		updateDateAndTime();
		
		spinner_adapter.notifyDataSetChanged();
		
		//TODO: preencher o resto dos Dados
		slidingDrawer.close();
		
		
		return true;
	}
	
	public void updateDateAndTime() {
		TextView date_view = (TextView) findViewById(R.id.new_appointment_date);
		date_view.setText(date);
		
        Spinner hour_spinner = (Spinner) findViewById(R.id.new_appointment_hour);
        //String arr[] = new String[0];
        spinner_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinner_hours);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour_spinner.setAdapter(spinner_adapter); 
		
		spinner_adapter.notifyDataSetChanged();
		
        hour_spinner.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						hour = (String) arg0.getItemAtPosition(arg2);						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}
		
        		}
        );
		
	}
	
	
	
	
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    	
		private Context context;
	
    	@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

    	private String[] groups = new String[0];
    	private ArrayList<ArrayList<User>> children = new ArrayList<ArrayList<User>>();

        public MyExpandableListAdapter(Context c){
        	
        	context = c;
        	
        	//map de data e con
        	Map<String, ArrayList<User>> map =  api.getDoctorsAndSpecialties();

			
			if(map.size() == 0){
				Toast toast = Toast.makeText(getApplicationContext(), "You don't have any appointments", Toast.LENGTH_LONG);
        		toast.show();
        		return;
			}
			
			
			populateAdapter(map);

        
        }
        
        public Object getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition);
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

            TextView textView = new TextView(CreateAppointment.this);
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
        	children = new ArrayList<ArrayList<User>>();
        }
        
        public void populateAdapter(Map<String, ArrayList<User>> map){
        	
        	groups = new String[map.size()];
			children = new ArrayList<ArrayList<User>>();
        	
        	int i = 0;	
			for(String key: map.keySet()){//este ciclo so vai correr 1 vez...

				groups[i] = key;
				children.add(map.get(key));
				i++;

			}	
        }

    }

}
	


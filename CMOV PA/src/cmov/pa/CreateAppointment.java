package cmov.pa;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class CreateAppointment extends ExpandableListActivity implements OnDrawerOpenListener, OnDrawerCloseListener{
	
	Api api;
	
	private RelativeLayout relativeLayout;
	private SlidingDrawer slidingDrawer;
	ExpandableListAdapter mAdapter;
	
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
		
		
		//TODO: preencher o resto dos Dados
		slidingDrawer.close();
		
		
		return true;
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
	


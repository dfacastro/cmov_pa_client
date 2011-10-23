package cmov.pa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class MedicAppointmentsTab  extends ExpandableListActivity {

	Api api;
	ExpandableListAdapter mAdapter;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		api = ((Api)getApplicationContext());
		
		
		mAdapter = new MyExpandableListAdapter();
	    setListAdapter(mAdapter);
	    setContentView(R.layout.appointments_tab);		
        
       
    }
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		User o = (User)mAdapter.getChild(groupPosition, childPosition);
		
		System.out.println(o.getId());
		
		Intent intent = new Intent(getApplicationContext(),ProfileTab.class);
		intent.putExtra("id", o.getId());
        startActivity(intent);
		
		return true;
	}
	
	
	// Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.medic_appointment_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.new_schedule:
				Intent intent = new Intent(getApplicationContext(),NewSchedule.class);
	            startActivity(intent);
	            finish();
	            
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

	
	/**
     * A simple adapter which maintains an ArrayList of photo resource Ids. 
     * Each photo is displayed as an image. This adapter supports clearing the
     * list of photos and adding a new photo.
     *
     */
	
	
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    	
    	private String[] groups;
    	private ArrayList<User> children;

        public MyExpandableListAdapter(){
        	Calendar c = Calendar.getInstance(); 
        	int year = c.get(Calendar.YEAR);
        	int month = c.get(Calendar.MONTH) + 1 ;
        	int day = c.get(Calendar.DAY_OF_MONTH);
        	
        	String date = year + "-" + month + "-" + day;
        	
        	try {
        		//TODO: alterar para a date
				Map <String, User> map = api.getAppointmentsForDate("2011-10-11");
				
				groups = new String[map.size()];
				children = new ArrayList<User>();
				
				
				int i = 0;
				for(String key: map.keySet()){
					
					groups[i] = key;		
					User u = map.get(key);		
					children.add(u);
					i++;
				}
				
			
        	} catch (ClientProtocolException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
        		toast.show();
			} catch (IOException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
        		toast.show();
			}
        
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

            TextView textView = new TextView(MedicAppointmentsTab.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
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

    }
	

}

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

	//TODO: por progress dialogs quando esta a actualizar...
	
	Api api;
	ExpandableListAdapter mAdapter;
	String currentDate;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		api = ((Api)getApplicationContext());
		
		setContentView(R.layout.medic_appointments_tab);
		
		mAdapter = new MyExpandableListAdapter();
	    setListAdapter(mAdapter);
	    
	    (findViewById(R.id.appointmentNextButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextBusyDay();
			}
        });
	    
	    
	    
	    (findViewById(R.id.appointmentPreviousButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//previousBusyDay();
			}
        });
	    	
        
       
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
		    case R.id.doctor_appointments_refresh:				
		    	updateAppointments();       
		        return true;
		    
		    case R.id.doctor_jump_to_date:
		        jumpToDate();
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

	
	
	
	public void updateAppointments(){
		
		Calendar c = Calendar.getInstance(); 
    	int year = c.get(Calendar.YEAR);
    	int month = c.get(Calendar.MONTH) + 1 ;
    	int day = c.get(Calendar.DAY_OF_MONTH);
    	
    	String date = year + "-" + month + "-" + day;
		
    	try {
			Map<String, Map <String, User>> map = api.getDoctorAppointmentsForDate(date);
			currentDate = date;
			
			((TextView)findViewById(R.id.appointmentDay)).setText(date);
			
			((MyExpandableListAdapter) mAdapter).reset();
			((MyExpandableListAdapter) mAdapter).notifyDataSetChanged();
			
			if(map.size() == 0){
				Toast toast = Toast.makeText(getApplicationContext(), "Nao tem appointments para hoje", Toast.LENGTH_LONG);
        		toast.show();
        		return;
			}

			((MyExpandableListAdapter) mAdapter).populateAdapter(map);
		
		
    	} catch (ClientProtocolException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
    		toast.show();
		} catch (IOException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
    		toast.show();
		}
	}
	
	
	
	public void nextBusyDay(){
		
    	try {
			Map<String, Map <String, User>> map = api.getDoctorAppointmentsNextBusyDay(currentDate);
			//currentDate = date;
			
			//((TextView)findViewById(R.id.appointmentDay)).setText(date);
			
			((MyExpandableListAdapter) mAdapter).reset();
			((MyExpandableListAdapter) mAdapter).notifyDataSetChanged();
			((MyExpandableListAdapter) mAdapter).populateAdapter(map);
		
		
    	} catch (ClientProtocolException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
    		toast.show();
		} catch (IOException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter appointments", Toast.LENGTH_SHORT);
    		toast.show();
		}
	}
	
	
	public void jumpToDate(){
		
		try {
			ArrayList<String> tmp = api.getDoctorBusyDays();
			System.out.println(tmp.toString());
			
			
			//as conversoes a seguir sao necessarias senao o java da erro
			//a funcao toArray() nao funciona bem, da erro de cast... tem de se fazer a la pata
			String s[] = new String[tmp.size()];
			for(int i = 0; i < tmp.size(); i++){
				s[i] = tmp.get(i);
			}
			
			final String[] items = s;
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Pick a Date");
			dialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	
			        String selectedItem = items[item];
			    	
			    	Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
			        
			    	try {
			    		
			    		Map<String, Map <String, User>> map = api.getDoctorAppointmentsForDate(selectedItem);
						currentDate = selectedItem;
						
						((TextView)findViewById(R.id.appointmentDay)).setText(selectedItem);
						
						((MyExpandableListAdapter) mAdapter).reset();
						((MyExpandableListAdapter) mAdapter).notifyDataSetChanged();
						((MyExpandableListAdapter) mAdapter).populateAdapter(map);					
					} catch (ClientProtocolException e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter busydays", Toast.LENGTH_SHORT);
			    		toast.show();
					} catch (IOException e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter busydays", Toast.LENGTH_SHORT);
			    		toast.show();
					}
			    	
			    	dialog.dismiss();
			        
			    }
			});
			dialog.show();
			
		} catch (IOException e1) {
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter busydays", Toast.LENGTH_SHORT);
    		toast.show();
		}
		
		
	}
	
	
	
	
	
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    	
    	@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		private String[] groups = new String[0];
    	private ArrayList<User> children = new ArrayList<User>();

        public MyExpandableListAdapter(){
        	Calendar c = Calendar.getInstance(); 
        	int year = c.get(Calendar.YEAR);
        	int month = c.get(Calendar.MONTH) + 1 ;
        	int day = c.get(Calendar.DAY_OF_MONTH);
        	
        	String date = year + "-" + month + "-" + day;
        	
        	try {
				Map<String, Map <String, User>> map = api.getDoctorAppointmentsForDate(date);
				currentDate = date;

				((TextView)findViewById(R.id.appointmentDay)).setText(date);
				
				if(map.size() == 0){
					Toast toast = Toast.makeText(getApplicationContext(), "Nao tem appointments para hoje", Toast.LENGTH_LONG);
	        		toast.show();
	        		return;
				}
				
				populateAdapter(map);

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
        
        public void reset(){
        	groups = new String[0];
        	children = new ArrayList<User>();
        }
        
        public void populateAdapter(Map<String, Map <String, User>> map){
        	
        	int i = 0;	
			for(String key: map.keySet()){//este ciclo so vai correr 1 vez...
				
				Map<String,User> submap = (Map<String, User>) map.get(key);
				
				groups = new String[submap.size()];
				children = new ArrayList<User>();

				
				for(String keys: submap.keySet()){
					groups[i] = keys;		
					User u = submap.get(keys);		
					children.add(u);
					i++;
				}
				
				
			}	
        }

    }
	

}

package cmov.pa;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileTab extends Activity {
	
	Api api;
	boolean selfprofile = true;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tab);
        
		api = ((Api)getApplicationContext());
		
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null)
		{
			String value = extras.getString("id");
			selfprofile = false;
			//TODO: faz set das coisas na interface do user que fez parse
			User user = api.getPatientProfile(value);
			if(user != null){
				setProfileOnInterface(user);
			}else{//caso o pedido de mal
				
				Toast toast = Toast.makeText(getApplicationContext(), "Error obtaining profile", Toast.LENGTH_LONG);
        		toast.show();
				finish();
			}
			
		}else{
			setProfileOnInterface(api.user);
		}
			

		
		
        
    }
	
	
	public void setProfileOnInterface(User user){
		//faz set do conteudo do profile
				((TextView)findViewById(R.id.profileName)).setText(user.getName());
				((TextView)findViewById(R.id.profileUsername)).setText(user.getUsername());
				((TextView)findViewById(R.id.profileBirthDate)).setText(user.getBirthDate());
				
				//medico
				if(user.isDoctor()){
			
					
					 URL newurl;

					Bitmap mIcon_val;
					try {
						
						newurl = new URL(user.getPhoto());
						mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
						((ImageView)findViewById(R.id.profilePhoto)).setImageBitmap(mIcon_val);
						
						
						

						
						
					} catch (IOException e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Error downloading image", Toast.LENGTH_SHORT);
			     		toast.show();
					} 


					((TextView)findViewById(R.id.profileAdress)).setText("");
					((TextView)findViewById(R.id.profileSex)).setText("");
				
				}else{//paciente
					((TextView)findViewById(R.id.profileAdress)).setText(user.getAddress());
					((TextView)findViewById(R.id.profileSex)).setText(user.getSex());
				}
	}
	
	
	
	
	
	@Override
	public void onBackPressed() {
		
		if(selfprofile){
		
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
	
		}else
			finish();
	}

}

package cmov.pa;

import cmov.pa.database.DatabaseAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CMOVPAActivity extends Activity implements Runnable {
		
	ProgressDialog dialog;
	Api api;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        api = new Api();
        
        (findViewById(R.id.loginConnectButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				connectAction();
			}
        });
        
        
        (findViewById(R.id.loginRegistButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				registAction();
			}
        });
                
        
    }
    
    public void connectAction(){
    	
			
		dialog = ProgressDialog.show(CMOVPAActivity.this, "", "Loading. Please wait...", true);
		Thread thread = new Thread(this);
        thread.start();

    }
    
    
    public void registAction(){
    	
    	Intent intent = new Intent(getApplicationContext(),Registo.class);
        startActivity(intent);
    }

	@Override
	public void run() {	
		
		EditText user = (EditText) findViewById(R.id.loginUser); 
		EditText pass = (EditText) findViewById(R.id.loginPass); 
		
		int success = api.login(user.getText().toString(), pass.getText().toString());	
		
		if(success == -3){
			
			dialog.dismiss();
			
			Looper.prepare();
			Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_SHORT);
    		toast.show();
    		Looper.loop();
    		
		}else if(success == -2){
			
			dialog.dismiss();
			
			Looper.prepare();
    		Toast toast = Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT);
    		toast.show();
    		Looper.loop();
		}else if (success == 0) {
			
			boolean retsuccess = api.getProfile();
			
			if(retsuccess){
					
					api.dbAdapter = new DatabaseAdapter(this);
				
					boolean bdsuccess = true;
					//boolean bdsuccess = api.updateDBIFNecessary();
					if(bdsuccess){
					
					dialog.dismiss();
					
					Intent intent = new Intent(getApplicationContext(),Inicial.class);
		            startActivity(intent);
		            finish();
				}else{
					
					dialog.dismiss();
					
					Looper.prepare();
					Toast toast = Toast.makeText(getApplicationContext(), "Error updating database", Toast.LENGTH_SHORT);
		    		toast.show();
					Looper.loop();

				}
					
			}else{
				
				dialog.dismiss();
				
				Looper.prepare();
				Toast toast = Toast.makeText(getApplicationContext(), "Error in downloading profile", Toast.LENGTH_SHORT);
	    		toast.show();
				Looper.loop();

			}
			
			
		}else if (success == -1){
			
			dialog.dismiss();
			
			Looper.prepare();
			Toast toast = Toast.makeText(getApplicationContext(), "Insert your credentials or register", Toast.LENGTH_SHORT);
    		toast.show();
			Looper.loop();
		
		}
		
		
	}
       
}


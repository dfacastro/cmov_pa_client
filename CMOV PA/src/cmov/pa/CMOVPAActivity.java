package cmov.pa;

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
		
		dialog.dismiss();
		
		if(success == -3){
			Looper.prepare();
			Toast toast = Toast.makeText(getApplicationContext(), "Erro a conectar com o servidor", Toast.LENGTH_SHORT);
    		toast.show();
    		Looper.loop();
    		
		}else if(success == -2){
			Looper.prepare();
    		Toast toast = Toast.makeText(getApplicationContext(), "Autenticacao Errada", Toast.LENGTH_SHORT);
    		toast.show();
    		Looper.loop();
		}else if (success == 0) {
			
			boolean retsuccess = api.getProfile();
			
			if(retsuccess){
				Intent intent = new Intent(getApplicationContext(),Inicial.class);
	            startActivity(intent);
	            finish();
			}else{
				Looper.prepare();
				Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter profile", Toast.LENGTH_SHORT);
	    		toast.show();
				Looper.loop();

			}
			
			
		}else if (success == -1){
			Looper.prepare();
			Toast toast = Toast.makeText(getApplicationContext(), "Insira as credenciais ou registe-se", Toast.LENGTH_SHORT);
    		toast.show();
			Looper.loop();
		
		}
		
		
	}
       
}


package cmov.pa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CMOVPAActivity extends Activity {
		
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
    	
			
		EditText user = (EditText) findViewById(R.id.loginUser); 
		EditText pass = (EditText) findViewById(R.id.loginPass); 
		
		int success = api.login(user.getText().toString(), pass.getText().toString());
		
		if(success == -2){
    		Toast toast = Toast.makeText(getApplicationContext(), "Autenticacao Errada", Toast.LENGTH_SHORT);
    		toast.show();
		}else if (success == 0) {
			
			boolean retsuccess = api.getProfile();
			
			if(retsuccess){
				Intent intent = new Intent(getApplicationContext(),Inicial.class);
	            startActivity(intent);
	            finish();
			}else{
				Toast toast = Toast.makeText(getApplicationContext(), "Erro a obter profile", Toast.LENGTH_SHORT);
	    		toast.show();
			}
			
			
		}else if (success == -1){
			Toast toast = Toast.makeText(getApplicationContext(), "Insira as credenciais ou registe-se", Toast.LENGTH_SHORT);
    		toast.show();
		}

    	
    }
    
    
    public void registAction(){
    	
    	Intent intent = new Intent(getApplicationContext(),Registo.class);
        startActivity(intent);
    }
    
    
    
    
    
    
    
}
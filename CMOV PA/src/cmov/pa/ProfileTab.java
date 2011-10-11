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
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tab);
        
		api = ((Api)getApplicationContext());
		
		
		//faz set do conteudo do profile
		((TextView)findViewById(R.id.profileName)).setText(api.user.getName());
		((TextView)findViewById(R.id.profileUsername)).setText(api.user.getUsername());
		((TextView)findViewById(R.id.profileBirthDate)).setText(api.user.getBirthDate());
		
		//medico
		if(api.user.isDoctor()){
	
			
			 URL newurl;

			Bitmap mIcon_val;
			try {
				
				newurl = new URL(api.user.getPhoto());
				mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
				((ImageView)findViewById(R.id.profilePhoto)).setImageBitmap(mIcon_val);
				
				
				

				
				
			} catch (IOException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Erro a carregar a imagem", Toast.LENGTH_SHORT);
	     		toast.show();
			} 


			((TextView)findViewById(R.id.profileAdress)).setText("");
		
		}else{//paciente
			((TextView)findViewById(R.id.profileAdress)).setText(api.user.getAddress());
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

}

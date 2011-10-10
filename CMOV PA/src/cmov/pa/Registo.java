package cmov.pa;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Registo extends Activity {
		
	Api api;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registo);
        
        api = ((Api)getApplicationContext());

        
        //por as opcoes no spinner
        String[] items = new String[] {"Male", "Female"};
        Spinner spinner = (Spinner) findViewById(R.id.registSexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        EditText dateET = (EditText) findViewById( R.id.registDataNasc );
        dateET.setText("aaaa-mm-dd");
        
        
        (findViewById(R.id.registRegistButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				registAction();
			}
        });
        
    }
    
    
    public void registAction(){
    	String user, pass, passconf, nome, sexo, datanasc, morada;
    	
    	
    	user = ((EditText) findViewById(R.id.registUser)).getText().toString();
    	pass = ((EditText) findViewById(R.id.registPass)).getText().toString();
    	passconf = ((EditText) findViewById(R.id.registPassConf)).getText().toString();
    	nome = ((EditText) findViewById(R.id.registNome)).getText().toString();
    	datanasc = ((EditText) findViewById(R.id.registDataNasc)).getText().toString();
    	morada = ((EditText) findViewById(R.id.registMorada)).getText().toString();
    	sexo = ((Spinner) findViewById(R.id.registSexo)).getSelectedItem().toString();
    	
    	//data tem de ser com dd-mm-aaaa
    	
    	if(user.length() == 0 || pass.length() == 0 || passconf.length() == 0 || nome.length() == 0 || datanasc.length() == 0 || morada.length() == 0){
    		Toast toast = Toast.makeText(getApplicationContext(), "Preencha todos os dados", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
    	}

    	if(!pass.equals(passconf)){
    		Toast toast = Toast.makeText(getApplicationContext(), "As passwords tem de ser iguais", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
    	}
    	
    	
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        if (datanasc.trim().length() != dateFormat.toPattern().length()){
        	Toast toast = Toast.makeText(getApplicationContext(), "A data invalida", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
        }

        dateFormat.setLenient(false);
        
        try {
          //parse the date parameter
          dateFormat.parse(datanasc.trim());
          
          /*
          Date date = new Date(dateFormat.toString());
          if(date.getYear() < 1900){
        	  Toast toast = Toast.makeText(getApplicationContext(), "A data invalida", Toast.LENGTH_SHORT);
      		  toast.show(); 
          }
          */
          
          
          //TODO: meter data minima a 1900
        }
        catch (java.text.ParseException e) {
        	Toast toast = Toast.makeText(getApplicationContext(), "A data invalida", Toast.LENGTH_SHORT);
    		toast.show();
        	return;
		}
        
    	api.regist(user, pass, nome, datanasc, morada, sexo);
    	

    	Toast toast = Toast.makeText(getApplicationContext(), "Registado com sucesso", Toast.LENGTH_LONG);
		toast.show();
    	
    	Intent intent = new Intent(getApplicationContext(),CMOVPAActivity.class);
        startActivity(intent);
        finish();
    }

}

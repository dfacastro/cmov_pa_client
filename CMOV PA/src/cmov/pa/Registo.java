package cmov.pa;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Registo extends Activity {
		
	Api api;
	
	private TextView mDateDisplay;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 1;

	
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
        
        (findViewById(R.id.registRegistButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				registAction();
			}
        });
        
        
        Button pickDate = (Button) findViewById(R.id.registDataNascimentoButton);
        pickDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        showDialog(DATE_DIALOG_ID);
                }
        });
        
        
        mDateDisplay = (TextView) findViewById(R.id.registBirthDateTV);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        
    }
   
    
    public void registAction(){
    	String user, pass, passconf, nome, sexo, datanasc, morada;
    	
    	
    	user = ((EditText) findViewById(R.id.registUser)).getText().toString();
    	pass = ((EditText) findViewById(R.id.registPass)).getText().toString();
    	passconf = ((EditText) findViewById(R.id.registPassConf)).getText().toString();
    	nome = ((EditText) findViewById(R.id.registNome)).getText().toString();
    	datanasc = ((TextView) findViewById(R.id.registBirthDateTV)).getText().toString();
    	morada = ((EditText) findViewById(R.id.registMorada)).getText().toString();
    	sexo = ((Spinner) findViewById(R.id.registSexo)).getSelectedItem().toString();
    	
    	//data tem de ser com dd-mm-aaaa
    	
    	if(user.length() == 0 || pass.length() == 0 || passconf.length() == 0 || nome.length() == 0 || datanasc.length() == 0 || morada.length() == 0){
    		Toast toast = Toast.makeText(getApplicationContext(), "Fill all the form camps", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
    	}

    	if(!pass.equals(passconf)){
    		Toast toast = Toast.makeText(getApplicationContext(), "The passwords must be the same", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
    	}
 
        
    	String errors = api.regist(user, pass, nome, datanasc, morada, sexo);
    	

    	if (errors.equals("")) {
    		Toast toast = Toast.makeText(getApplicationContext(), "Registed with success", Toast.LENGTH_LONG);
    		toast.show();
    	} else {
    		Toast toast = Toast.makeText(getApplicationContext(), errors, Toast.LENGTH_LONG);
    		toast.show();
    		return;
    	}
    	
    	Intent intent = new Intent(getApplicationContext(),CMOVPAActivity.class);
        startActivity(intent);
        finish();
    }
    
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
            switch (id) {

            case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
            }
            return null;
    }
    
    
    protected void onPrepareDialog(int id, Dialog dialog) {
            switch (id) {

            case DATE_DIALOG_ID:
                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
            }
    }    
    
    
    private void updateDisplay() {
            mDateDisplay.setText(
                    new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" "));
    }
    
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
            }
    };

}

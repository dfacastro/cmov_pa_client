package cmov.pa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewWorkDay extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_workday);
        
        String[] items = new String[] {"Male", "Female"};
        Spinner weekday = (Spinner) findViewById(R.id.new_workday_weekday_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekday.setAdapter(adapter);   
    };
	

}

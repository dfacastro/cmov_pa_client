package cmov.pa;

import java.util.ArrayList;
import java.util.List;

import utils.WorkDay;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkDaysAdapter extends ArrayAdapter<WorkDay> {

    int resource;
    String response;
    Context context;
    
    //Initialize adapter
    public WorkDaysAdapter(Context context, int resource, ArrayList<WorkDay> items) {
        super(context, resource, items);
        this.resource=resource; 
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout workdaysView;
        WorkDay workday = getItem(position);
 
        //Inflate the view
        if(convertView==null)
        {
        	workdaysView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, workdaysView, true);
        }
        else
        {
        	workdaysView = (LinearLayout) convertView;
        }
        //Get the text boxes from the listitem.xml file
        TextView wdWeekDay = (TextView)workdaysView.findViewById(R.id.workday_weekday);
        TextView wdStartEnd =(TextView)workdaysView.findViewById(R.id.workday_start_end);
 
        //Assign the appropriate data from our alert object above
        wdWeekDay.setText(workday.wday.toString());
        wdStartEnd.setText(workday.getStartString() + " - " + workday.getEndString());
 
        return workdaysView;
    }
}

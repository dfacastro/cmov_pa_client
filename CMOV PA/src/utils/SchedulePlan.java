package utils;

import java.util.Vector;

public class SchedulePlan {
	public Vector<WorkDay> workdays = new Vector<WorkDay>();
	
	
	public WorkDay[] getArray() {
		WorkDay wdarray[] = new WorkDay[0];
		return workdays.toArray(wdarray);
	}
	

}

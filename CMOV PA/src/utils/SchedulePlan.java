package utils;

import java.util.Collections;
import java.util.Vector;

public class SchedulePlan {
	public Vector<WorkDay> workdays = new Vector<WorkDay>();
	
	
	public WorkDay[] getArray() {
		WorkDay wdarray[] = new WorkDay[0];
		return workdays.toArray(wdarray);
	}
	
	public void add(WorkDay new_wd) {
		
		//verifica se há sobreposição
		for(int i = 0; i < workdays.size(); i++)
			if(workdays.get(i).overlaps(new_wd)) {
				WorkDay wd = workdays.remove(i);
				wd.merge(new_wd);
				workdays.add(wd);
				
				checkForMerges();
				Collections.sort(workdays);
				
				return;
			}
		
		workdays.add(new_wd);
		Collections.sort(workdays);
	}
	
	private void checkForMerges() {
		
		for(int i = 0; i < workdays.size(); i++)
			for(int j = 0; j < workdays.size(); j++) {
				if(i != j) {
					
					if(workdays.get(i).overlaps(workdays.get(j))) {
						WorkDay wd = workdays.get(i);
						WorkDay wd2 = workdays.get(j);
						
						workdays.remove(wd);
						workdays.remove(wd2);
						
						wd.merge(wd2);
						workdays.add(wd);
					}
				}
			}
				
				
	}
	

}

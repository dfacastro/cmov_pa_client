package utils;

import java.io.Serializable;

public class WorkDay implements Serializable, Comparable<WorkDay> {
	public WeekDay wday;
	public int start;
	public int end;
	
	public WorkDay(WeekDay wday, int start, int end) {
		this.wday = wday;
		this.start = start;
		this.end = end;		
	}
	
	public WorkDay() {
		
	}
	
	public String getStartString() {
		String ret =  Integer.toString(start/60) + ":";
		String mins = Integer.toString(start%60);
		if(mins.equals("0"))
			ret+= "00";
		else
			ret+= mins;
		return ret;
	}
	
	public String getEndString() {
		String ret =  Integer.toString(end/60) + ":";
		String mins = Integer.toString(end%60);
		if(mins.equals("0"))
			ret+= "00";
		else
			ret+= mins;
		return ret;
	}
	
	public boolean overlaps(WorkDay wd) {
		
		if(wd.wday != this.wday)
			return false;
		
		if((wd.start >= start && wd.start <= end) || (wd.end >= start && wd.end <= end) )
			return true;
		
		return false;
	}
	
	public void merge(WorkDay wd) {
		if(wd.start < this.start)
			this.start = wd.start;
		if(wd.end > this.end)
			this.end = wd.end;
		
	}

	@Override
	public int compareTo(WorkDay wd) {
		
		if(wday.compareTo(wd.wday) < 0 )
			return -1;
		else if (wday.compareTo(wd.wday) > 0 )
			return 1;
		
		if(start < wd.start)
			return -1;
		if(start > wd.start)
			return 1;
		else
			return 0;
		
		
		
	}
	
	public boolean equals(WorkDay wd) {
		if(wd.wday.equals(wday) && wd.start == start && wd.end == end)
			return true;
		return false;
		
	}
	
}

package utils;

public class WorkDay {
	public WeekDay wday;
	public int start;
	public int end;
	
	public WorkDay(WeekDay wday, int start, int end) {
		this.wday = wday;
		this.start = start;
		this.end = end;		
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
}

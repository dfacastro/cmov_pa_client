package utils;

public enum WeekDay {
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	
	public static String[] toStringArray() {
		WeekDay wds[] = WeekDay.values();
		String s[] = new String[wds.length];
		
		for(int i = 0; i < wds.length; i++)
			s[i] = wds[i].name();
		
		return s;
		
	}
}

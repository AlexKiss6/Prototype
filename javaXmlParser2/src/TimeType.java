//import java.util.*;

public class TimeType {
	private static int time;
	
	public int getTime(){
		return time;
	}
	
	public String getStringTime(){
		String Time, Suffix = " A.M";
		int temp;
		
		temp = time/60;
		
		if (temp > 12){
			temp -= 12;
			Suffix = " P.M";
		} else if (temp == 12){
			Suffix = " P.M";
		}
		
		Time = Integer.toString(temp) + ":";
		
		temp = time % 60;
		if (temp < 10){
			Time += "0";
		}
		Time += Integer.toString(temp);
		Time += Suffix;
		
		return Time;
	}
	
	public void setTime(int param){
		if (param < 1400 && param > 0){
			time = param;
		}
		else{
			time = 0;
		}
	}
	
	public void setTime(String param){
		int hour, minute;
		hour = Integer.valueOf(param.substring(0, param.indexOf(":")));
		System.out.println(hour);
		minute = Integer.valueOf(param.substring(param.indexOf(":") + 1, param.indexOf(" ")));
		System.out.println(minute);
		if (param.contains("P.M") && hour != 12){
			System.out.println(true);
			hour += 12;
		}
		setTime((hour*60) + minute);
	}
	
	TimeType(){
		time = 0;
	}
	
	TimeType(int param){
		setTime(param);
	}
}

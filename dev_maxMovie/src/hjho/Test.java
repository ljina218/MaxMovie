package hjho;

import java.util.Calendar;

public class Test {
	
	public String setYMD() {
		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return yyyy+"년"+
				(mm < 10 ? "0"+mm:""+mm)+"월"+
				(day < 10 ? "0"+day:""+day)+"일";
	}//end of setTimer
	
	public String setHMS() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		return (hour < 10 ? "0"+hour:""+hour)+"시"+
			   (min < 10 ? "0"+min:""+min)+"분";
	}//end of setTimer
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		
	}

}

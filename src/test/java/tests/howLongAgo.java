package tests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class howLongAgo {
	public static void main(String[] args) {
		String dateInput = "3 days ago";//Originally Posted : 1 day ago
		int num =0;

		if(!dateInput.equalsIgnoreCase("Moments ago")){
			String postedTime = dateInput.replace("Originally Posted : ", "");
			postedTime = postedTime.replace(" ago", "");
			num = Integer.parseInt(postedTime.replaceAll("[^0-9.]", ""));
			int len = (num + "").length();
			String timeMeasure = postedTime.substring(len);
			System.out.println("timeMeasure = " + timeMeasure);

			int multiplier=(timeMeasure.contains("month"))? 30: (timeMeasure.contains("year"))? 365:(timeMeasure.contains("day"))?1:0;
			num*=multiplier;
		}
		System.out.println("num = " + num);

		LocalDate now= LocalDate.now();
		DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");
		now=now.minusDays(num);
		System.out.println("now = " + now);


	}
}

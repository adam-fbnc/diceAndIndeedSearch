package runners;

import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class dup {
/*
	*/
/**//*
public void jobDetails(){
		int size=jobElements.size();
		System.out.println("size = " + size);
		System.out.println("coNames.size() = " + coNames.size());
		LocalDate now= LocalDate.now();
		DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");


		for (int i =0; i<size; i++) {
			WebElement job=jobElements.get(i);
			WebElement jobNum=jobIDs.get(i);
			WebElement company=coNames.get(i);
			WebElement location=locations.get(i);
			String jobTitle=job.getText();
			String jobUrl =job.getAttribute("href");
			String jobID=jobNum.getAttribute("id");
			String companyName=company.getText();
			String jobLocation=location.getAttribute("data-rc-loc");

			System.out.println(i+1+") jobTitle :"+jobTitle+" - companyName :" + companyName+" - Date :"+ now.format(dtf)+" - Job ID :"+jobID+"\nJob location:" +jobLocation+ " - URL :"+jobUrl);
			System.out.println();
		}

	}
*/

}

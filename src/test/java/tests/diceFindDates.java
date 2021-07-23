package tests;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.Driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class diceFindDates {
	protected WebDriver driver = Driver.getDriver();

	@Test
	public void findJobDates() throws Exception {

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String availableURL = "";
		String jobTitle = "";
		String companyName = "";

			File file = new File("ApplicationTracking.xlsm");
			FileInputStream fileInputStream = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheet("JobsSheet");
			int lastRowNoDate = 2+(int) sheet.getRow(0).getCell(6).getNumericCellValue();
			int count = (int) sheet.getRow(0).getCell(8).getNumericCellValue();
			System.out.println("lastRowNoDate = " + lastRowNoDate);
			System.out.println("count = " + count);
		LocalDate now= LocalDate.now();
		DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");
		count=50;

		for (int i = lastRowNoDate; i < lastRowNoDate + count; i++) {
			availableURL = sheet.getRow(i).getCell(5).getStringCellValue();
			driver.get("url");
			WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
			WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
			WebElement jobNum = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()-1]/div"));
			int pDays = thisManyDaysAgo(postedTime.getText());
			int oDays = thisManyDaysAgo(originallyPosted.getText());
			String date2=(now.minusDays(pDays)).format(dtf);
			String date1=(now.minusDays(oDays)).format(dtf);
			String jobID=jobNum.getText();
			sheet.getRow(i).getCell(6).setCellValue(date2);
			sheet.getRow(i).getCell(7).setCellValue(date1);
		}
//        driver.close();

		FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		fileInputStream.close();
		workbook.close();
	}
	public int thisManyDaysAgo(String dateInput){
		int num =0;
		if(!dateInput.equalsIgnoreCase("Moments ago")){
			String postedTime = dateInput.replace("Originally Posted : ", "");
			postedTime = postedTime.replace(" ago", "");
			num = Integer.parseInt(postedTime.replaceAll("[^0-9.]", ""));
			int len = (num + "").length();
			String timeMeasure = postedTime.substring(len);
//            System.out.println("timeMeasure = " + timeMeasure);
			int multiplier=(timeMeasure.contains("month"))? 30: (timeMeasure.contains("year"))? 365:(timeMeasure.contains("day"))?1:(timeMeasure.contains("week"))?7:0;
			num*=multiplier;
		}
//        System.out.println("num = " + num);
		return num;
	}
}

package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import utilities.Driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class diceOpenSearchResultPage2 {
	WebDriver driver= Driver.getDriver();

@Test
	public void Test() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.dice.com/jobs?q=Selenium%20Java%20Testing&countryCode=US&radius=30&radiusUnit=mi&page=1&pageSize=50&filters.postedDate=ONE&language=en");
//		Thread.sleep(1500);
		List<WebElement> jobElements = driver.findElements(By.xpath("//a[@class='card-title-link bold']"));
		WebElement job=jobElements.get(0);
		String jobUrl=job.getAttribute("href");
		String a="window.open('"+jobUrl+"','_blank');";
		((JavascriptExecutor)driver).executeScript(a);
//		Thread.sleep(1500);
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1)); //switches to new tab
		WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
		WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
		WebElement jobNum = driver.findElement(By.xpath("//div[@class='company-header-info']/div[4]/div"));
		System.out.println("postedTime.getText() = " + postedTime.getText()+ " and the time: "+" and the time: "+thisManyDaysAgo(postedTime.getText()));
		System.out.println("originallyPosted.getText() = " + originallyPosted.getText()+" and the time: "+thisManyDaysAgo(originallyPosted.getText()));
		driver.close();

	}

	public int thisManyDaysAgo(String dateInput){
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

		return num;
	}
}

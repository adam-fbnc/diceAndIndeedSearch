package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import utilities.Driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class indeedOpenSearchResultPage {

	@Test
	public void Test() throws InterruptedException {
		WebDriver driver= Driver.getDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");
//		Thread.sleep(3000);
//		System.out.println("jobElements.size() = " + jobElements.size());
		List<WebElement> jobElements = driver.findElements(By.xpath("//a[@class='jobtitle turnstileLink ']"));
		WebElement job=jobElements.get(0);
		String url1=job.getText();
//		link1.sendKeys(Keys.CONTROL +"t");
		System.out.println("url1 = " + url1);

		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
		job.click();
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0)); //switches to new tab
		Thread.sleep(1000);
		driver.switchTo().window(tabs.get(1)); //switches to new tab
		Thread.sleep(1000);
		driver.close();
	}
}

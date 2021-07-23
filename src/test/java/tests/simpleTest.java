package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import utilities.Driver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class simpleTest {



	@Test
	public void Test(){
		WebDriver driver = Driver.getDriver();
		driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");
		List<WebElement> jobElements = driver.findElements(By.xpath("//a[@class='jobtitle turnstileLink ']"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		System.out.println("covidPromo.getText() = " + covidPromo.getText());
//		System.out.println("covidPromo.getAttribute(\"href\") = " + covidPromo.getAttribute("href"));
		System.out.println("jobElements.size() = " + jobElements.size());
		String a="window.open('https://www.dice.com','_blank');";
//		String a="window.open(https://www.dice.com,'_blank');";
//		String a="window.open("+url1+",'_blank');";
		((JavascriptExecutor)driver).executeScript(a);
	}
}

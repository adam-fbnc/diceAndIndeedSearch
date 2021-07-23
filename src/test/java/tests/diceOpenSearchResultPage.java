package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import utilities.Driver;

import java.util.concurrent.TimeUnit;

public class diceOpenSearchResultPage {
	WebDriver driver= Driver.getDriver();

	@FindBy(xpath = "//li/a[@href='/abtest']")
	public WebElement link1;

	@FindBy(linkText="A/B Testing") public WebElement l1;

	@FindBy(linkText = "Add/Remove Elements")
	public WebElement link2;
	@FindBy(linkText = "Autocomplete")
	public WebElement link3;

	@Test
	public void Test() {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://practice.cybertekschool.com/");
//		String url1=link1.getText();
//		link1.sendKeys(Keys.CONTROL +"t");
//		System.out.println("url1 = " + url1);
		System.out.println("link1 = " + link1);
		System.out.println("l1 = " + l1);
		System.out.println("link2 = " + link2);
		System.out.println("link2 = " + link2.getText());
//		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
	}
}

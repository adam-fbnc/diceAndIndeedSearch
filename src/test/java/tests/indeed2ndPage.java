package tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class indeed2ndPage {
    protected WebDriver driver = Driver.getDriver();

    public indeed2ndPage(){
        PageFactory.initElements(driver, this);
    }
@Test
    public void jobDetails() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=50");
            List<WebElement> jobIDs= driver.findElements(
                    By.xpath("//div[@class='jobsearch-SerpJobCard unifiedRow row result clickcard']"));
            List<WebElement> jobElements=driver.findElements(
                    By.xpath("//div[@class='jobsearch-SerpJobCard unifiedRow row result clickcard']/h2/a"));
            List<WebElement> coNames= driver.findElements(By.xpath("//div[@class='jobsearch-SerpJobCard unifiedRow row result clickcard']/div/div/span[1]"));
            List<WebElement> locations= driver.findElements(By.className("recJobLoc"));
        int size=jobIDs.size();
        System.out.println("size = " + size);
        System.out.println("coNames.size() = " + coNames.size());

        for (int i =0; i<size; i++) {
            String jobID = jobIDs.get(i).getAttribute("id");
            String jobTitle = jobElements.get(i).getAttribute("title");
            String jobUrl = jobElements.get(i).getAttribute("href");
            String companyName = coNames.get(i).getText();
            String jobLocation = locations.get(i).getAttribute("data-rc-loc");
            System.out.println("i = " + i);
            System.out.println("jobID = " + jobID);
            System.out.println("jobTitle = " + jobTitle);
            System.out.println("companyName = " + companyName);
            System.out.println("jobLocation = " + jobLocation);
            System.out.println("jobUrl = " + jobUrl);
        }

    }

}

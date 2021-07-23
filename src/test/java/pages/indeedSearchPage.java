package pages;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class indeedSearchPage {
    protected WebDriver driver = Driver.getDriver();

    public indeedSearchPage(){
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "searchCountPages")
    public WebElement jobCountWE;

    public int login(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");
        driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");

        String totalJobCount = jobCountWE.getText();
        System.out.println(totalJobCount);
        totalJobCount=totalJobCount.substring(10).replaceAll("[^0-9.]", "");
        int jobCount = Integer.parseInt(totalJobCount);
//        System.out.println(totalJobCount);
//        System.out.println("jobCount = " + jobCount);
        int pages = jobCount/50+1;
        System.out.println("pages = " + pages);
        return pages;
    }

    public void search(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");

    }

    public void jobDetails(String url) throws IOException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
            List<WebElement> jobIDs= driver.findElements(
                    By.xpath("//div[@class='jobsearch-SerpJobCard unifiedRow row result clickcard']"));
            List<WebElement> jobElements=driver.findElements(
                    By.xpath("//div[@class='jobsearch-SerpJobCard unifiedRow row result clickcard']/h2/a"));
            List<WebElement> coNames= driver.findElements(By.xpath("//div[1]/div[1]/span[@class='company']"));
            List<WebElement> locations= driver.findElements(By.className("recJobLoc"));
        System.out.println("coNames.size() = " + coNames.size());
            LocalDate now= LocalDate.now();
            DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");
        File file = new File("ApplicationTracking.xlsm");
        FileInputStream fileInputStream=new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet= workbook.getSheet("JobsSheet");
        int lastUsedRow= sheet.getLastRowNum();
        int j=0;
        for (int i =0; i<coNames.size(); i++) {
            String jobID=jobIDs.get(i).getAttribute("id");
            String jobTitle=jobElements.get(i).getAttribute("title");
            String jobUrl =jobElements.get(i).getAttribute("href");
            String companyName=coNames.get(i).getText();
            String jobLocation=locations.get(i).getAttribute("data-rc-loc");
            String todaysDate=now.format(dtf);
            System.out.println("i = " + i);
            System.out.println("jobID = " + jobID);
            System.out.println("jobTitle = " + jobTitle);
            System.out.println("companyName = " + companyName);
            System.out.println("jobLocation = " + jobLocation);
            System.out.println("jobUrl = " + jobUrl);
                lastUsedRow++;
                XSSFRow row=sheet.createRow(lastUsedRow);
                row.createCell(0).setCellValue(jobTitle);
                row.createCell(1).setCellValue(companyName);
                row.createCell(2).setCellValue("Indeed");
                row.createCell(3).setCellValue(jobLocation);
                row.createCell(4).setCellValue(jobID);
                row.createCell(5).setCellValue(jobUrl);
                row.createCell(6).setCellValue(todaysDate);
                row.createCell(7).setCellValue(todaysDate);
            }

        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();

    }

}

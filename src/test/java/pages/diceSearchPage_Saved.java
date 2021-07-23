package pages;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.URLEncodeDecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class diceSearchPage_Saved {
    protected WebDriver driver = Driver.getDriver();

    public diceSearchPage_Saved(){
        PageFactory.initElements(driver, this);
    }

    //------------------->> LOGIN
    @FindBy(id="email")        public WebElement userNameElement;
    @FindBy(id="password")        public WebElement passwordElement;
    @FindBy(xpath="//button[@type='submit']")        public WebElement submitButtonElement;
    public void login() throws InterruptedException {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(ConfigurationReader.getProperty("url"));
        String userName= ConfigurationReader.getProperty("diceUsername");
        String password= ConfigurationReader.getProperty("dicePassword");
        userNameElement.sendKeys(userName);
        passwordElement.sendKeys(password);
        submitButtonElement.click();
        Thread.sleep(3000);
        boolean pageTitle=driver.getTitle().equals("Dashboard Home Feed | Dice.com");
        if(pageTitle){
            System.out.println("Login sucessful!");
        }
        Assert.assertTrue(pageTitle);
    }

    //------------------->> GET THE NUMBER OF RESULTS
    @FindBy(id = "totalJobCount")       public WebElement jobCountWE;
    public int countVacancies(String url, int numPerPage){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        String totalJobCount = jobCountWE.getText();
        System.out.println(totalJobCount);
        totalJobCount=totalJobCount.replaceAll(",","");
        int jobCount = Integer.parseInt(totalJobCount);
        /*        System.out.println(totalJobCount);
            System.out.println("jobCount = " + jobCount);*/
        return jobCount/numPerPage+1;
    }

    //------------------->> CHECK ALL JOB DETAILS
    public void jobDetails(String url) throws IOException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
            List<WebElement> jobElements =driver.findElements(By.xpath("//a[@class='card-title-link bold']"));
            List<WebElement> coNames =driver.findElements(By.xpath("//div[1]/div/div[2]/div[1]/div/a[@data-cy='search-result-company-name']"));
            List<WebElement> locations =driver.findElements(By.id ("searchResultLocation"));
            int size=jobElements.size();
            System.out.println("size = " + size);
            System.out.println("coNames.size() = " + coNames.size());
        LocalDate now= LocalDate.now();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");
            File file = new File("ApplicationTracking.xlsm");
            FileInputStream fileInputStream=new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet= workbook.getSheet("JobsSheet");
        int lastUsedRow= sheet.getLastRowNum();
            System.out.println("lastUsedRow = " + lastUsedRow);

        for (int i =0; i<size; i++) {
            WebElement job=jobElements.get(i);
            WebElement company=coNames.get(i);
            WebElement location=locations.get(i);
                String jobTitle=job.getText();
                String jobUrl =job.getAttribute("href");
                String companyName=company.getText();
                String jobLocation=location.getText();
                String todaysDate=now.format(dtf);
                String a="window.open('"+jobUrl+"','_blank');";
            ((JavascriptExecutor)driver).executeScript(a);
            ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1)); //switches to new tab
                WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
                WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
                WebElement jobNum = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()-1]/div"));
            int pDays = thisManyDaysAgo(postedTime.getText());
            int oDays = thisManyDaysAgo(originallyPosted.getText());
            String date2=(now.minusDays(pDays)).format(dtf);
            String date1=(now.minusDays(oDays)).format(dtf);
            String jobID=jobNum.getText();
            driver.close();

            lastUsedRow++;
            XSSFRow row=sheet.createRow(lastUsedRow);
            XSSFCell cell1=row.createCell(0);
            XSSFCell cell2=row.createCell(1);
            XSSFCell cell3=row.createCell(2);
            XSSFCell cell4=row.createCell(3);
            XSSFCell cell5=row.createCell(4);
            XSSFCell cell6=row.createCell(5);
            XSSFCell cell7=row.createCell(6);
            XSSFCell cell8=row.createCell(7);

            cell1.setCellValue(jobTitle);
            cell2.setCellValue(companyName);
            cell3.setCellValue("Dice");
            cell4.setCellValue(jobLocation);
            cell5.setCellValue(jobID);
            cell6.setCellValue(jobUrl);
            cell7.setCellValue(date2);
            cell8.setCellValue(date1);
            System.out.println((i+1)+") Title: "+jobTitle+" - Company: "+companyName);//+" - Location: "+jobLocation+"\n   Today's date: "+todaysDate+" - URL: "+jobUrl);
            driver.switchTo().window(tabs.get(0));
        }

/*        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); //switches to new tab
        driver.get(jobUrl);
        String timePosted = postedTime.getText();
        System.out.println("timePosted = " + timePosted);*/

        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();
    }

    //------------------->> CONVERT "HOURS/DAYS/WEEKS/MONTHS AGO" TO DATES
    public int thisManyDaysAgo(String dateInput){
        int num =0;
        if(!dateInput.contains("moments ago")){
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

    //------------------->> PART 1: GET JOB/COMPANY/LOCATION/JOBID/CO_ID, BUT NOT POST_DATE & ORIG_DATE
    public void someJobDetails(String url) throws Exception {
        driver.get(url);
        List<WebElement> jobElements =driver.findElements(By.xpath("//a[@class='card-title-link bold']"));
        List<WebElement> coNames =driver.findElements(By.xpath("//div[1]/div/div[2]/div[1]/div/a[@data-cy='search-result-company-name']"));
        List<WebElement> locations =driver.findElements(By.id ("searchResultLocation"));

        int size=jobElements.size();
        System.out.println("size = " + size);
        System.out.println("coNames.size() = " + coNames.size());
        LocalDate now= LocalDate.now();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");

        File file = new File("ApplicationTracking.xlsm");
        FileInputStream fileInputStream=new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet= workbook.getSheet("JobsSheet");
        int lastUsedRow= sheet.getLastRowNum();
        System.out.println("lastUsedRow = " + lastUsedRow);
/*            int diceResults= (int) sheet.getRow(0).getCell(3).getNumericCellValue();
            String[] jobIdsX=new String[diceResults];
            String[] coIdsX=new String[diceResults];
            String[] jobNames=new String[diceResults];
            String[] jobSites=new String[diceResults];

            for (int k = 0; k < diceResults; k++) {
                XSSFRow row=sheet.getRow(k+2);
                jobNames[k] = row.getCell(0).getStringCellValue();
                jobSites[k] = row.getCell(3).getStringCellValue();
                jobIdsX[k] = row.getCell(10).getStringCellValue();
                coIdsX[k]= row.getCell(9).getStringCellValue();

        String jobIdsX="6465";
        String coIdsX="RTL102837";
        String jobNamesX="Senior API Platform Architect/Developer";
        String jobSitesX="Tempe, AZ, USA";*/

        for (int i =0; i<size; i++) {
            WebElement job=jobElements.get(i);
            WebElement company=coNames.get(i);
            WebElement location=locations.get(i);
            String jobTitleW=job.getText();
            String jobUrlW =job.getAttribute("href");
//            jobUrlW= URLEncodeDecode.decode(jobUrlW);
            String[] jobDetailsW=jobUrlW.split("/");
            int directoryEnds = Arrays.asList(jobDetailsW).indexOf("detail");
/*            System.out.println("directoryEnds = " + directoryEnds);
            System.out.println("jobDetailsW.length = "+jobDetailsW.length);*/
            String jobIdsW = URLEncodeDecode.decode(jobDetailsW[directoryEnds+3]);
            jobIdsW= jobIdsW.substring(0,jobIdsW.indexOf("?"));
            String coIdsW= jobDetailsW[directoryEnds+2];
            String companyNameW=company.getText();
            String jobLocationW=location.getText();
            String todaysDate=now.format(dtf);
/*                        String a="window.open('"+jobUrl+"','_blank');";
                        ((JavascriptExecutor)driver).executeScript(a);
                        ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
                        driver.switchTo().window(tabs.get(1)); //switches to new tab
                        WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
                        WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
                        WebElement jobNum = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()-1]/div"));
                       int pDays = thisManyDaysAgo(postedTime.getText());
                        int oDays = thisManyDaysAgo(originallyPosted.getText());
                        String date2=(now.minusDays(pDays)).format(dtf);
                        String date1=(now.minusDays(oDays)).format(dtf);
                        String jobID=jobNum.getText();
                        driver.close();*/
            lastUsedRow++;
            XSSFRow row=sheet.createRow(lastUsedRow);
            XSSFCell cell1=row.createCell(0);
            XSSFCell cell2=row.createCell(1);
            XSSFCell cell3=row.createCell(2);
            XSSFCell cell4=row.createCell(3);
            XSSFCell cell5=row.createCell(4);
            XSSFCell cell6=row.createCell(5);
            XSSFCell cell12=row.createCell(11);
            /*XSSFCell cell7=row.createCell(6);
            XSSFCell cell8=row.createCell(7);
            cell7.setCellValue(date2);
            cell8.setCellValue(date1);*/
            cell1.setCellValue(jobTitleW);
            cell2.setCellValue(companyNameW);
            cell3.setCellValue("Dice");
            cell4.setCellValue(jobLocationW);
            cell5.setCellValue(jobIdsW);
            cell6.setCellValue(jobUrlW);
            cell12.setCellValue(coIdsW);

            System.out.println("\n"+(i+1)+") Title: "+jobTitleW+" - Company: "+companyNameW+" - Location: "+jobLocationW+"\n   Today's date: "+todaysDate+" - URL: "+jobUrlW);
            System.out.println("jobIdsW = " + jobIdsW);
            System.out.println("coIdsW = " + coIdsW);
//                            for (int j = 0; j < jobDetails.length; j++) {System.out.println((i+1)+"-"+(j+1)+": "+"jobDetails[j] = " + jobDetails[j]);}

        }
//            }
        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
        workbook.close();
    }

    //------------------->> PART 2: GET POST_DATE & ORIG_DATE OF NON DUPLICATE JOBS
    public void findJobDates() throws Exception {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            File file = new File("ApplicationTracking.xlsm");
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheet("JobsSheet");
            int firstRowNoDate = 2+(int) sheet.getRow(0).getCell(6).getNumericCellValue();
            int count = (int) sheet.getRow(0).getCell(8).getNumericCellValue();
            System.out.println("lastRowNoDate = " + firstRowNoDate);
            System.out.println("count = " + count);
        LocalDate now= LocalDate.now();
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM/dd/yyyy");
//        count=148;
        for (int i = firstRowNoDate; i < firstRowNoDate + count; i++) {
            String theLink = sheet.getRow(i).getCell(5).getHyperlink().getAddress();
            driver.get(theLink);
            WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
            WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
            int pDays = thisManyDaysAgo(postedTime.getText());
            int oDays = thisManyDaysAgo(originallyPosted.getText());
            String date2=(now.minusDays(pDays)).format(dtf);
            String date1=(now.minusDays(oDays)).format(dtf);
            sheet.getRow(i).createCell(6).setCellValue(date2);
//            sheet.getRow(i).getCell(6).setCellValue(date2);
            sheet.getRow(i).createCell(7).setCellValue(date1);
//            sheet.getRow(i).getCell(7).setCellValue(date1);
        }
//        driver.close();

        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
        workbook.close();
    }


    public void search(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start=0");

    }


}

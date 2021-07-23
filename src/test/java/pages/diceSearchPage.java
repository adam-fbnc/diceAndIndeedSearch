package pages;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.ConfigurationReader;
import utilities.Driver;
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

public class diceSearchPage {
    protected WebDriver driver = Driver.getDriver();

    public diceSearchPage(){
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
        Thread.sleep(1500);
        boolean pageTitle=driver.getTitle().equals("Dashboard Home Feed | Dice.com");
        if(pageTitle){
            System.out.println("Login sucessful!");
        }
        Assert.assertTrue(pageTitle);
    }

    //------------------->> GET THE NUMBER OF RESULTS
    @FindBy(id = "totalJobCount")       public WebElement jobCountWE;
    public int countVacancies(String url, int numPerPage){
        driver.get(url);
        String totalJobCount = jobCountWE.getText();
        System.out.println(totalJobCount);
        totalJobCount=totalJobCount.replaceAll(",","");
        int jobCount = Integer.parseInt(totalJobCount);
        return jobCount/numPerPage+1;
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

        for (int i =0; i<size; i++) {
            WebElement job=jobElements.get(i);
            WebElement company=coNames.get(i);
            WebElement location=locations.get(i);
            String jobTitleW=job.getText();
            String jobUrlW =job.getAttribute("href");
            String[] jobDetailsW=jobUrlW.split("/");
            int directoryEnds = Arrays.asList(jobDetailsW).indexOf("detail");
            String jobIdsW = jobDetailsW[directoryEnds+3];
            jobIdsW= decodedString(jobIdsW.substring(0,jobIdsW.indexOf("?")));
            jobIdsW= decodedString(jobIdsW);
            String coIdsW= jobDetailsW[directoryEnds+2];
            String companyNameW=company.getText();
            String jobLocationW=location.getText();
            String todaysDate=now.format(dtf);

            lastUsedRow++;
            XSSFRow row=sheet.createRow(lastUsedRow);
            row.createCell(0).setCellValue(jobTitleW);
            row.createCell(1).setCellValue(companyNameW);
            row.createCell(2).setCellValue("Dice");
            row.createCell(3).setCellValue(jobLocationW);
            row.createCell(4).setCellValue(jobIdsW);
            row.createCell(5).setCellValue(jobUrlW);
            row.createCell(11).setCellValue(coIdsW);

            System.out.println("\n"+(i+1)+") Title: "+jobTitleW+" - Company: "+companyNameW+" - Location: "+jobLocationW+"\n   Today's date: "+todaysDate+" - URL: "+jobUrlW);
            System.out.println("jobIdsW = " + jobIdsW);
            System.out.println("coIdsW = " + coIdsW);

        }

        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
        workbook.close();
    }

    //------------------->> PART 2: GET POST_DATE & ORIG_DATE OF NON DUPLICATE JOBS
    @FindBy(xpath="(//button[@class='btn btn-primary btn-lg dice-btn apply disableButton'])[1]")
    public WebElement applyButton;
    public void findJobDates() throws Exception {
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
//        count=150;
        for (int i = firstRowNoDate; i < firstRowNoDate + count; i++) {
            String theLink = sheet.getRow(i).getCell(5).getHyperlink().getAddress();
            driver.get(theLink);
                        String appURL = applyButton.getAttribute("id");
                        String applicationResult="";
                        String outsideURL="";
                        Thread.sleep(1000);
                        if(appURL.equalsIgnoreCase("applybtn")){
                            applicationResult="O,N";
                            outsideURL= applyButton.getAttribute("onclick")
                                    .replace("sendApply('www.dice.com', '","")
                                    .replace("')","");
                            sheet.getRow(i).createCell(8).setCellValue(applicationResult);
                            sheet.getRow(i).createCell(13).setCellValue(outsideURL);
                        }
            WebElement postedTime = driver.findElement(By.xpath("//ul/li[@class='posted ']/span"));
            WebElement originallyPosted = driver.findElement(By.xpath("//div[@class='company-header-info']/div[last()]/div"));
            int pDays = thisManyDaysAgo(postedTime.getText());
            int oDays = thisManyDaysAgo(originallyPosted.getText());
            String date2=(now.minusDays(pDays)).format(dtf);
            String date1=(now.minusDays(oDays)).format(dtf);
            sheet.getRow(i).createCell(6).setCellValue(date2);
            sheet.getRow(i).createCell(7).setCellValue(date1);
        }
        FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
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

    //------------------->> REPLACE SPECIAL CHARACTER CODES WITH ACTUAL SP.CHAR

    @Test
    public void simpleTest( ){
        String s="https://www.dice.com/jobs/detail/java-developer-scigon-solutions-ann-arbor%2C-mi-mi/10377716/OOJ%26%2332%26%2345%26%23321089%26%234590?searchlink=search%2F%3Fq%3DSelenium%2520Java%2520Testing%26countryCode%3DUS%26radius%3D30%26radiusUnit%3Dmi%26page%3D1%26pageSize%3D500%26filters.postedDate%3DONE%26language%3Den&searchId=b7bdf6ec-8a12-4e77-8a48-160ac83b5227";
        System.out.println(decodedString(decodedString(s)));
    }
    public String decodedString(String inputString){
        inputString=inputString.replaceAll("%3F","?");
        inputString=inputString.replaceAll("%20"," ");
        inputString=inputString.replaceAll("&#45","-");
        inputString=inputString.replaceAll("&#32","-");
        inputString=inputString.replaceAll("%25", "%");
        inputString=inputString.replaceAll("%26","&");
        inputString=inputString.replaceAll("%3D","=");
        inputString=inputString.replaceAll("%7B","{");
        inputString=inputString.replaceAll("%7D","}");
        inputString=inputString.replaceAll("%5B","[");
        inputString=inputString.replaceAll("%5D","]");
        inputString=inputString.replaceAll("%28","(");
        inputString=inputString.replaceAll("%29",")");
        inputString=inputString.replaceAll("&%2345","-");
        inputString=inputString.replaceAll("&%2347","/");
        inputString=inputString.replaceAll("%2C",",");
        inputString=inputString.replaceAll("%20"," ");
        inputString=inputString.replaceAll("%2F","/");
        inputString=inputString.replaceAll("%7C","|");
        inputString=inputString.replaceAll("%24","$");
        inputString=inputString.replaceAll("%3A",":");
        inputString=inputString.replaceAll("%2B","+");
        inputString=inputString.replaceAll("%23","#");
        inputString=inputString.replaceAll("%2A","*");
        inputString=inputString.replaceAll("%40","@");
        inputString=inputString.replaceAll("%3B",";");
        inputString=inputString.replaceAll("%21","!");
        inputString=inputString.replaceAll("%27","'");

        return inputString;
    }
}

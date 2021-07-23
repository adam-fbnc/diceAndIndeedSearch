package pages;

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

import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class diceApply {
	protected WebDriver driver= Driver.getDriver();
	public diceApply(){		PageFactory.initElements(driver,this);	}

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
		Thread.sleep(1000);
		boolean pageTitle=driver.getTitle().equals("Dashboard Home Feed | Dice.com");
		if(pageTitle){
			System.out.println("Login sucessful!");
		}
		Assert.assertTrue(pageTitle);
	}

	@FindBy(xpath="(//button[@class='btn btn-primary btn-lg dice-btn apply disableButton'])[1]")
	public WebElement applyButton;
	@FindBy(xpath="//*[@id='submit-job-btn']")
	public WebElement applyBtn2;
	@FindBy(xpath = "(//div/span[@class='bfh-selectbox-option'])[1]")
	public WebElement selectResume;
	@FindBy(xpath = "//a[.='Software Development Engineer(Last Updated: 01/11/2021)']")
	public WebElement resume;
	@FindBy(xpath = "(//div/span[@class='bfh-selectbox-option'])[2]")
	public WebElement selectCL;
	@FindBy(xpath = "//a[.='SDET Cover Letter (Last Updated: 12/01/2020)']")
	public WebElement coverLetter;
	@FindBy(xpath = "//h4[.='Success. Your application is on its way.']")
	public WebElement success;

	public String apply(String URL)throws InterruptedException {
		Random rand = new Random();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		/*String jobID="7173_98481";
		String coID = "10166753";
		URL="https://www.dice.com/jobs/detail/"+coID+"/"+jobID;*/
		driver.get(URL);
		String appURL = applyButton.getAttribute("id");
		String applicationResult="";
// IF APPLYBUTTON WEBELEMENT HAS APPLYBTN ID, CLICKING ON THE 'APPLY' BUTTON
// WILL RESULT IN REDIRECTION. USER WILL HAVE TO APPLY ON ANOTHER WEBSITE
		if(appURL.equalsIgnoreCase("applybtn")){
			applicationResult="O,N|"+applyButton.getAttribute("onclick")
				.replace("sendApply('www.dice.com', '","")
				.replace("')","");
			Thread.sleep(1000+rand.nextInt(20)*100);
		}else{
			applyButton.click();
			selectResume.click();
			resume.click();
			selectCL.click();
			coverLetter.click();
			applyBtn2.click();
			Thread.sleep(3000+rand.nextInt(20)*3000);
			applicationResult=(success.isDisplayed())?"Y":"F";
		}

		return applicationResult;
	}

	public void applyAndUpdate() throws IOException, InterruptedException {
		login();
		String applyResult="";
		String externalURL="";
		File file = new File("ApplicationTracking.xlsm");
		FileInputStream fileInputStream=new FileInputStream(file);
		XSSFWorkbook workbook= new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheet("JobsSheet");
		int count =(int) sheet.getRow(0).getCell(12).getNumericCellValue();
//		count=1;
		for (int i = 2; i < count+2; i++) {
			String coID = sheet.getRow(i).getCell(11).getStringCellValue();
			String jobID = sheet.getRow(i).getCell(4).getStringCellValue();
			String URL="https://www.dice.com/jobs/detail/"+coID+"/"+jobID;
//			sheet.getRow(i).createCell(6).setCellValue(date2);
			applyResult =apply(URL);
			if(applyResult.contains("O,N|")){
				externalURL=applyResult.replace("O,N|","");
				sheet.getRow(i).createCell(13);
				sheet.getRow(i).getCell(13).setCellValue(externalURL);
				applyResult="O,N";
				System.out.println("externalURL = " + externalURL);
				System.out.println("applyResult = " + applyResult);
			}
			sheet.getRow(i).createCell(8).setCellValue(applyResult);
			sheet.getRow(i).createCell(12).setCellValue("");
		}

		FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		fileInputStream.close();
		workbook.close();
	}


}

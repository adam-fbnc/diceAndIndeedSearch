package tests;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class excelTests {
	public static void main(String[] args) throws IOException {
		File file = new File("ApplicationTracking.xlsx");
		FileInputStream fileInputStream=new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet= workbook.getSheet("JobsSheet");
		//ROW AND CELL[COLUMN] REFERENCES BEGIN WITH "0"
//		System.out.println(sheet.getRow(1).getCell(1));

		int usedCells = sheet.getPhysicalNumberOfRows();
		int lastUsedRow= sheet.getLastRowNum();
		XSSFRow row=sheet.createRow(lastUsedRow+1);
		XSSFCell cell1=row.createCell(0);
		XSSFCell cell2=row.createCell(1);
		XSSFCell cell3=row.createCell(2);
		XSSFCell cell4=row.createCell(3);
		XSSFCell cell5=row.createCell(4);
		XSSFCell cell6=row.createCell(5);
		XSSFCell cell7=row.createCell(6);
		XSSFCell cell8=row.createCell(7);

		cell1.setCellValue("Java Selenium Automation Test Analyst");
		cell2.setCellValue("Infosys Limited");
		cell3.setCellValue("Indeed");
		cell4.setCellValue("Raleigh, NC");
		cell5.setCellValue("pj_5490870980daff31");
		cell6.setCellValue("https://www.indeed.com/pagead/clk?mo=r&ad=-6NYlbfkN0DFi1nmQQWK2fa3N4W3y7EUOEocZkWPqKP_f_xZ7ne8RaqWQ7rFuLilFqrEb_XPBzX9rGZsJOqI_J0own17QMlhO212OnjLw7oEhJdF_4AqvmXzptzuX5t28Dp5SLIFbhrSpOOZm4qbBS8XZOJB3IV6ASgYyPCp6C-sLXGiS-7clYqOCvZa9-eVbcf_DhTyPzJZECXHUKiY7UYF-jGtduZ52xCA1z5HHxnL7z7qP5O24y6ZOoJrAGQ6PG_xMo4RswocEF4wRJycGDgYmaMDlnHGDnUsRjPDoEGbg0XBPWT4NCPDkV3ZGdlXXaGp--Fa2fhX8PUlVB1t6v4ZttbZGJxXWOZy4dYNmASTYrfANg6OhbLBMSf5WGrc-9Q7O__BlUbLupaRwPVT6BHZDME1UlyV4HvQxMPiSKDCctIy3653SGGBoLS1cA5vB3aiP2wAogL9KcLSG9JU0W8gAnu41wO1eg34c7BoBjPsvFZ5F3oQ6GzfKASeAo4V-S1-Pr75S8-CLw0WS20E83CHK0VENyQjPhwuamu-F4qAIj6w05v3zQ==&p=0&fvj=0&vjs=3&tk=1eokr6gth350a000&jsa=2833");
		cell7.setCellValue("12/03/2020");
		cell8.setCellValue("12/03/2020");
		FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsx");
		workbook.write(fileOutputStream);
		fileInputStream.close();
		fileOutputStream.close();
		workbook.close();
	}
}

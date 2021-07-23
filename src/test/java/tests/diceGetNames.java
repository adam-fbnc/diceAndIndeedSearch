package tests;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class diceGetNames {

@Test
    public void getSpreadsheetInfo() throws IOException {

        File file = new File("ApplicationTracking.xlsm");
        FileInputStream fileInputStream=new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet= workbook.getSheet("JobsSheet");
        int diceResults= (int) sheet.getRow(0).getCell(3).getNumericCellValue();

        for (int i =0; i<diceResults; i++) {
                XSSFRow row=sheet.getRow(i+2);
                String jobDesc = row.getCell(0).getStringCellValue();
                String entireDesc = row.getCell(9).getStringCellValue();
                String cityAndState= row.getCell(3).getStringCellValue();

                jobDesc=jobDesc.replaceAll("/"," ").replaceAll(" ","-").toLowerCase();
                cityAndState=cityAndState.toLowerCase();
                String[] location=cityAndState.split(",");
                System.out.println("=============================================\n"+(i+1)+") entireDesc = " + entireDesc);
                System.out.println("cityAndState = " + cityAndState);

                int lenJD=jobDesc.length();
                int posLoc=entireDesc.length()+1;
                if(!location[0].equals("")){
                    if(entireDesc.contains(location[0].toLowerCase())) {
                        posLoc = entireDesc.indexOf(location[0]);
                        }
                    }
                System.out.println("location[0] = " + location[0]);
                System.out.println("posLoc = " + posLoc);
                String companyName=entireDesc.substring(lenJD+1,posLoc-1).replaceAll("-"," ");
                System.out.println("Company = " + companyName);
                row.getCell(1).setCellValue(companyName);
                }

                FileOutputStream fileOutputStream= new FileOutputStream("ApplicationTracking.xlsm");
                workbook.write(fileOutputStream);
                fileOutputStream.close();
                fileInputStream.close();
                workbook.close();
    }

}

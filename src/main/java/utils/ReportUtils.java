package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReportUtils {

    public static final String FILE_PATH = "TestReport.xlsx";

    public static void createExcelReport(int testCaseId, boolean isPassed, String errorMessage, long durationMills){

        Workbook workbook;
        Sheet sheet;

        try {

            File file = new File(FILE_PATH);

            // create sheet if it does not exist
            if(file.exists()){
                System.out.println("Opening existing report file: " + FILE_PATH);
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                System.out.println("Creating new report file: " + FILE_PATH);
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("TestReport");

                // create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Test Case");
                headerRow.createCell(1).setCellValue("Status");
                headerRow.createCell(2).setCellValue("Execution Time");
                headerRow.createCell(3).setCellValue("Description");
            }

            // getting next empty row
            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum + 1);

            // file row data
            row.createCell(0).setCellValue("TC_" + testCaseId);
            row.createCell(1).setCellValue(isPassed ? "Pass" : "Fail");
            row.createCell(2).setCellValue(durationMills);
            row.createCell(3).setCellValue(isPassed ? "N/A" : errorMessage);

            Row headerRow = sheet.getRow(0);
            for(int i = 0 ; i < headerRow.getLastCellNum() ; i++){
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();


            System.out.println("Excel report generated successfully at: " + FILE_PATH);
        } catch (Exception e) {
            System.out.println("Error while writing test result for " + testCaseId);
            e.printStackTrace();
        }
    }
}

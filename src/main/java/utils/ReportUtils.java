package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ReportUtils {

    public static void createExcelReport(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TestReport");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Test Case");
            headerRow.createCell(1).setCellValue("Status");
            headerRow.createCell(2).setCellValue("Start Time");
            headerRow.createCell(3).setCellValue("Execution Time");
            headerRow.createCell(4).setCellValue("Description");


            // Header background color
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (Cell cell : headerRow) cell.setCellStyle(headerStyle);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            System.out.println("Excel report created successfully at: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTestResults(File file, String testCase, String status, double duration, String description) {
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum + 1);

            row.createCell(0).setCellValue("TC_" + testCase);
            row.createCell(1).setCellValue(status);
            row.createCell(2).setCellValue(CommonUtilities.getCurrentTime());
            row.createCell(3).setCellValue(duration);
            row.createCell(4).setCellValue(description);

            for (int i = 0; i < 5; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            System.out.println("Data added for: " + testCase);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

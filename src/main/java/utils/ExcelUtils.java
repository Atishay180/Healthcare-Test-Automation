package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    public static List<Map<String, String>> getTestData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            System.out.println("Opening Excel file: " + filePath);
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                workbook.close();
                fis.close();
                throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file.");
            }

            System.out.println("Reading sheet: " + sheetName);

            //read header row
            Row headerRow = null;
            int headerRowIndex = -1;
            int executerColIndex = -1;

            //identify dynamic headerRow, executerColIndex and headerRowIndex
            for(int i = 0 ; i <= sheet.getLastRowNum() ; i++){
                Row row = sheet.getRow(i);

                if(row == null) continue;

                for(int j = 0 ; j < row.getLastCellNum() ; j++){
                    Cell cell = row.getCell(j);

                    if(cell != null && cell.toString().equalsIgnoreCase("Executer")){
                        headerRow = row;
                        headerRowIndex = i;
                        executerColIndex = j;
                        System.out.println("Found header row at index: " + headerRowIndex + ", Executer column index: " + executerColIndex);
                        break;
                    }
                }

                //header found
                if(headerRow != null) break;
            }

            //check if sheet is empty or all executer is no
            if(headerRow == null || executerColIndex == -1){
                workbook.close();
                fis.close();
                throw new RuntimeException("No header row with 'Executer' column found in sheet: " + sheetName);
            }

            int colCount = headerRow.getLastCellNum();

            System.out.println("Extracting data rows...");

            //iterate all row
            for(int i = headerRowIndex + 1 ; i <= sheet.getLastRowNum() ; i++){
                Row row = sheet.getRow(i);

                if(row == null) continue;

                Cell executerCell = row.getCell(executerColIndex);
                //skip if executer is no or null
                if(executerCell == null || !executerCell.toString().equalsIgnoreCase("Yes")){
                    System.out.println("Skipping row " + (i+1) + " (Executer = No/Blank)");
                    continue;
                }

                //add row value in a map
                Map<String, String> rowData = new HashMap<>();
                for(int j = 0 ; j < colCount ; j++){
                    Cell headerCell = headerRow.getCell(j);

                    if(headerCell == null) continue;

                    String header = headerCell.toString().trim();
                    Cell cell = row.getCell(j);
                    String value = cell == null ? "" : cell.toString().trim();

                    rowData.put(header, value);
                }

                dataList.add(rowData);
            }

            workbook.close();
            fis.close();

            System.out.println("Total executable rows extracted: " + dataList.size());


            return dataList;
        } catch (Exception e) {
            throw new RuntimeException("Error while reading Excel data: " + e.getMessage(), e);
        }
    }
}

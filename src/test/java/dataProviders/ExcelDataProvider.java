package dataProviders;

import gettingStarted.ProjectRunnerClass;
import org.testng.annotations.DataProvider;
import utils.ExcelUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {
    @DataProvider(name = "excelData")
    public static Iterator<Object[]> getExcelData() {
        String filePath = ProjectRunnerClass.sheetPath;
        String sheetName = ProjectRunnerClass.sheetName;

        List<Map<String, String>> testData = ExcelUtils.getTestData(filePath, sheetName);

        return testData.stream()
                .map(data -> new Object[]{data})
                .iterator();
    }
}

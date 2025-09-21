package gettingStarted;

import dataProviders.ExcelDataProvider;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.CommonUtilities;

import java.util.Map;

public class ProjectRunnerClass {
    public static String sheetPath = System.getProperty("user.dir") + "/src/test/resources/data/TestingAutomationExcelSheet.xlsx";
    public static String sheetName = "Sheet1";
    public static String TestCase = "";

    @BeforeSuite
    public void BeforeTestAction() {
        String folderPath = CommonUtilities.createTodaysDateFolder();
    }

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void UITest(Map<String, String> args) {
        String TestCase = args.get("Test Case");
        String folderPath = CommonUtilities.createTestCaseFolder(TestCase);

        System.out.println("Test Execution Started");
        System.out.println("Name = " + args.get("Name") + " | Email = " + args.get("Email"));
        System.out.println("Test execution completed");
    }
}

package gettingStarted;

import dataProviders.ExcelDataProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pages.AppointmentPage;
import pages.AuthPage;
import pages.HomePage;
import utils.CommonUtilities;

import java.util.Map;

public class ProjectRunnerClass {
    public static String sheetPath = System.getProperty("user.dir") + "/src/test/resources/data/TestingAutomationExcelSheet.xlsx";
    public static String sheetName = "Sheet1";
    public static String TestCase = "";

    WebDriver driver = new ChromeDriver();

    @BeforeSuite
    public void BeforeTestAction() {
        String folderPath = CommonUtilities.createTodaysDateFolder();
    }

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void UITest(Map<String, String> args) {
        try {
            String TestCase = args.get("Test Case");
            String folderPath = CommonUtilities.createTestCaseFolder(TestCase);

            driver.get("http://localhost:5173");
            driver.manage().window().fullscreen();

            HomePage homepage = new HomePage(driver, args);
            homepage.closeAlertBar();

            AuthPage authPage = homepage.getAuthPage();
            authPage.loginUser();

            if(args.get("Book Appointment").equalsIgnoreCase("Yes")){
                AppointmentPage appointmentPage = homepage.navigateToAppointmentPage(args.get("Doctor Name"));
                appointmentPage.bookAppointment();
            }
        } catch (Exception e) {
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            throw new RuntimeException(e);
        }

    }
}

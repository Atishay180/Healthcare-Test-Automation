package gettingStarted;

import dataProviders.ExcelDataProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import pages.AppointmentPage;
import pages.AuthPage;
import pages.HomePage;
import pages.ProfilePage;
import utils.CommonUtilities;
import utils.ReportUtils;

import java.io.File;
import java.util.Map;

public class ProjectRunnerClass {
    public static String sheetPath = System.getProperty("user.dir") + "/src/test/resources/data/TestingAutomationExcelSheet.xlsx";
    public static String sheetName = "Sheet1";
    public static String testCase = "";
    public static File reportFile;

    WebDriver driver;

    @BeforeSuite
    public void BeforeTestAction() {
        String folderPath = CommonUtilities.createTodaysDateFolder();
        reportFile = new File(folderPath, "TestReport.xlsx");
        if (!reportFile.exists()) {
            ReportUtils.createExcelReport(reportFile);
        }
        System.out.println("Report initialized at: " + reportFile.getAbsolutePath());
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void UITest(Map<String, String> args) {
        long startTime = System.currentTimeMillis();
        String testCase = args.get("Test Case");
        String status = "PASS";
        String description = "Executed successfully";

        try {
            testCase = args.get("Test Case");
            String folderPath = CommonUtilities.createTestCaseFolder(testCase);

            driver.get("http://localhost:5173");
            driver.manage().window().fullscreen();

            HomePage homepage = new HomePage(driver, args);
            homepage.closeAlertBar();

            AuthPage authPage = homepage.getAuthPage();
            authPage.loginUser();

            if(args.get("Book Appointment").equalsIgnoreCase("Yes") || args.get("Book Appointment").equalsIgnoreCase("Y")){
                AppointmentPage appointmentPage = homepage.navigateToAppointmentPage(args.get("Doctor Name"));
                appointmentPage.bookAppointment();
            }

            else if(args.get("My Appointments").equalsIgnoreCase("Yes") || args.get("My Appointments").equalsIgnoreCase("Y")){
                By profileDropdownLocator = By.id("profile-dropdown");
                CommonUtilities.waitUntilVisibleByLocator(driver, profileDropdownLocator);
                WebElement profileDropdownElement = driver.findElement(profileDropdownLocator);

                // Hover over the parent menu
                Actions actions = new Actions(driver);
                actions.moveToElement(profileDropdownElement).perform();

                WebElement myAppointments = driver.findElement(By.xpath("//div/p[text()=\"My Appointments\"]"));
                CommonUtilities.waitUntilVisible(driver, myAppointments);
                myAppointments.click();

                CommonUtilities.captureScreenshot("My Appointments", driver);
            }

            else if(args.get("My Profile").equalsIgnoreCase("Yes") || args.get("My Profile").equalsIgnoreCase("Y")){
                ProfilePage profilePage = homepage.navigateToProfilePage();

                if(args.get("Update Profile").equalsIgnoreCase("Yes") || args.get("Update Profile").equalsIgnoreCase("Y")){
                    profilePage.editProfile();
                }
            }
        } catch (Exception e) {
            status = "FAIL";
            description = e.getMessage();
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
        } finally {
            long endTime = System.currentTimeMillis();
            double duration = (endTime - startTime) / 1000.0;

            // Write result to Excel
            ReportUtils.writeTestResults(reportFile, testCase, status, duration, description);

            System.out.println("Test completed: " + testCase + " | Status: " + status);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // close browser after each test case
        }
    }
}

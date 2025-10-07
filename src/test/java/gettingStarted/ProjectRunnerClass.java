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
import utils.CommonUtilities;

import java.util.Map;

public class ProjectRunnerClass {
    public static String sheetPath = System.getProperty("user.dir") + "/src/test/resources/data/TestingAutomationExcelSheet.xlsx";
    public static String sheetName = "Sheet1";
    public static String TestCase = "";

    WebDriver driver;

    @BeforeSuite
    public void BeforeTestAction() {
        String folderPath = CommonUtilities.createTodaysDateFolder();
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
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

            if(args.get("My Appointments").equalsIgnoreCase("Yes")){
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
        } catch (Exception e) {
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // close browser after each test case
        }
    }
}

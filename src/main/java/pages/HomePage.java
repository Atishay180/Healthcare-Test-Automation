package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonUtilities;

import java.util.Map;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "close-prompt-button")
    private WebElement btnAlert;

    @FindBy(xpath = "//button[text()=\"Create Account\"]")
    private WebElement lnkAuthPage;

    @FindBy(xpath = "//div/h1[text()=\"Top doctors to book\"]")
    private WebElement lblTopDoctors;

    //dynamic xpath for doctor name
    public WebElement getDoctorLink(WebDriver driver, String doctorName){
        String dynamicPath = "//div/h3[text()='" + doctorName + "']";
        System.out.println("Looking for doctor: " + doctorName);
        return driver.findElement(By.xpath(dynamicPath));
    }

    public void closeAlertBar(){
        try {
            CommonUtilities.captureScreenshot("Alert Bar", driver);
            btnAlert.click();
        } catch (Exception e) {
            System.err.println("Error in alert bar" + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw new RuntimeException(e);
        }
    }

    public AuthPage getAuthPage(){
        try {
            CommonUtilities.captureScreenshot("Home Page", driver);
            lnkAuthPage.click();

            return new AuthPage(driver);
        } catch (Exception e) {
            System.err.println("Error in home page" + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw new RuntimeException(e);
        }
    }

    public void navigateToAppointmentPage(String doctorName){
        try {
            CommonUtilities.waitUntilVisible(driver, lblTopDoctors);
            CommonUtilities.scrollToElement(driver, lblTopDoctors);

            WebElement lnkDoctorAppointmentPage = getDoctorLink(driver, doctorName);

            CommonUtilities.waitUntilVisible(driver, lnkDoctorAppointmentPage);
            CommonUtilities.scrollToElement(driver, lnkDoctorAppointmentPage);

            CommonUtilities.captureScreenshot("Top Doctors", driver);

            lnkDoctorAppointmentPage.click();
        } catch (Exception e){
            System.err.println("Error in home page" + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw new RuntimeException(e);
        }
    }
}

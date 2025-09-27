package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonUtilities;

import java.util.Map;

public class AppointmentPage {
    private final WebDriver driver;
    private final Map<String, String> args;

    public AppointmentPage(WebDriver driver, Map<String, String> args){
        this.driver = driver;
        this.args = args;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div/p[contains(text(), \"Appointment fee\")]/span")
    private WebElement txtAppointmentFee;

    public WebElement getDoctorLink(String doctorName){
        String dynamicPath = "//div/p[text()='" + doctorName + "']";
        System.out.println("Looking for doctor: " + doctorName);
        return driver.findElement(By.xpath(dynamicPath));
    }

    public void bookAppointment(){
        try {
            CommonUtilities.captureScreenshot("Appointment Page", driver);

            WebElement txtDoctorName = getDoctorLink(args.get("Doctor Name"));

            // verify doctor name
            String doctorName = txtDoctorName.getText().trim();

            if (!args.get("Doctor Name").equalsIgnoreCase(doctorName)) {
                throw new AssertionError("Doctor Name mismatched. Expected: "
                        + args.get("Doctor Name") + " but found: " + doctorName);
            }
            System.out.println("Doctor Name verified successfully.");

            // verify appointment fee method
            //fee displayed
            String appointmentFee = txtAppointmentFee.getText().trim();
            String amountDisplayedStr = appointmentFee.replaceAll("[^0-9]", "");;
            int amountDisplayed = Integer.parseInt(amountDisplayedStr);

            //actual fee extracted from sheet
            String actualAmountStr = args.get("Appointment Fee");
            double actualAmountDouble = Double.parseDouble(actualAmountStr);
            int actualAmount = (int)actualAmountDouble;

            if (actualAmount != amountDisplayed) {
                throw new AssertionError("Appointment fee mismatched. Expected: "
                        + actualAmount + " but found: " + amountDisplayed);
            }
            System.out.println("Appointment Fee verified successfully.");

        } catch (AssertionError ae) {
            CommonUtilities.captureScreenshot("ValidationFailure", driver);
            driver.quit();
            throw ae;
        } catch (Exception e) {
            System.out.println("Error in appointment page: " + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw new RuntimeException(e);
        }
    }
}

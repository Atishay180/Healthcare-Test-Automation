package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import utils.CommonUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProfilePage {
    private final WebDriver driver;
    private final Map<String, String> args;
    public ProfilePage(WebDriver driver, Map<String, String> args) {
        this.driver = driver;
        this.args = args;
        PageFactory.initElements(driver, this);
    }

    @FindBy (xpath = "//button[text()=\"Edit\"]")
    private WebElement btnEditProfile;

    @FindBy (xpath = "//p[text()=\"Address\"]/following-sibling::p/input[1]")
    private WebElement txtAddressLane1;

    @FindBy (xpath = "//p[text()=\"Address\"]/following-sibling::p/input[2]")
    private WebElement txtAddressLane2;

    @FindBy (xpath = "//p[contains(text(),\"Gender\")]/following-sibling::select")
    private WebElement ddlGender;

    @FindBy (xpath = "//p[contains(text(), \"Birthday\")]/following-sibling::input")
    private WebElement txtDob;

    @FindBy (xpath = "//label/input[@id=\"image\"]")
    private WebElement fileUploadPath;

    @FindBy (xpath = "//button[text()=\"Save Information\"]")
    private WebElement btnSaveInformation;

    public void editProfile() {
        try {
            CommonUtilities.waitUntilVisible(driver, btnEditProfile);
            btnEditProfile.click();

            //edit contact information
            System.out.println("Updating Contact Information...");

            txtAddressLane1.clear();
            txtAddressLane1.sendKeys(args.get("Updated Address Lane 1"));

            txtAddressLane2.clear();
            txtAddressLane2.sendKeys(args.get("Updated Address Lane 2"));

            //edit basic information if yes
            System.out.println("Updating Basic Information...");

            //update gender
            String gender = args.get("Updated Gender");
            if(gender != null && !gender.isEmpty()){
                Select genderDropdown = new Select(ddlGender);
                genderDropdown.selectByValue(gender);
                WebElement selectedOption = genderDropdown.getFirstSelectedOption();
                System.out.println("Gender updated to: " + selectedOption.getText());
            }

            //update dob
            System.out.println("Updating DOB...");
            String dob = args.get("Updated DOB");

            txtDob.clear();
            txtDob.sendKeys(dob);
            System.out.println("DOB updated to: " + dob);

            //update profile pic if yes
            if(args.get("Update Image").equalsIgnoreCase("Yes") || args.get("Update Image").equalsIgnoreCase("Y")){
                CommonUtilities.uploadFile(args.get("ImageFile Name"), fileUploadPath);
            }

            //save
            CommonUtilities.waitUntilVisible(driver, btnSaveInformation);
            btnSaveInformation.click();

            CommonUtilities.waitUntilVisible(driver, btnEditProfile);
            CommonUtilities.captureScreenshot("Updated Profile", driver);
        } catch (Exception e) {
            System.err.println("Error in profile page: " + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw new RuntimeException(e);
        }
    }
}

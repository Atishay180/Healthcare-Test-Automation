package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonUtilities;

import java.util.Map;

public class AuthPage {
    private final WebDriver driver;
    private final Map<String, String> args;

    public AuthPage(WebDriver driver, Map<String, String> args){
        this.driver = driver;
        this.args = args;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//p[contains(text(), \"Create an new account?\")]/span")
    private WebElement lnkCreateAccountMenu;

    @FindBy(xpath = "//p/span[text()=\"Login here\"]")
    private WebElement lnkLoginMenu;

    @FindBy(xpath = "//p[text()=\"Full Name\"]/following-sibling::input")
    private WebElement txtFullName;

    @FindBy(xpath = "//p[text()=\"Email\"]/following-sibling::input")
    private WebElement txtEmail;

    @FindBy(xpath = "//p[text()=\"Password\"]/following-sibling::input")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[text()=\"Login\"]")
    private WebElement btnLogin;

    @FindBy(xpath = "//form/div/button[text()=\"Create Account\"]")
    private WebElement btnCreateAccount;

    public void loginUser() {
        try {
            // create a new account
            if(args.get("Create Account").equalsIgnoreCase("Yes")){
                txtFullName.sendKeys(args.get("Full Name"));
                txtEmail.sendKeys(args.get("Email"));
                txtPassword.sendKeys(args.get("Password"));

                CommonUtilities.captureScreenshot("Create Account", driver);

                btnCreateAccount.click();
            }
            // login existing user
            else {
                lnkLoginMenu.click();
                txtEmail.sendKeys(args.get("Email"));
                txtPassword.sendKeys(args.get("Password"));

                CommonUtilities.captureScreenshot("login", driver);

                btnLogin.click();
            }

        } catch (Exception e) {
            System.err.println("Error occurred during login: " + e.getMessage());
            CommonUtilities.captureScreenshot("TestCase Failure", driver);
            driver.quit();
            throw e;
        }
    }
}
